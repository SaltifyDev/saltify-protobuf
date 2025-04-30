package org.ntqqrev.saltify.protobuf.model

import org.ntqqrev.saltify.protobuf.Global
import org.ntqqrev.saltify.protobuf.ProtoMessage
import org.ntqqrev.saltify.protobuf.annotation.ProtoIgnore
import org.ntqqrev.saltify.protobuf.util.CodedReader
import org.ntqqrev.saltify.protobuf.util.CodedWriter
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.javaField

internal class ProtoModel<T : ProtoMessage>(val kClass: KClass<T>) {
    val descriptors: List<ProtoFieldDescriptor> =
        kClass.declaredMemberProperties
            .filter { property ->
                !(property.javaField?.annotations?.any { it is ProtoIgnore } ?: false)
            }
            .map {
                if (it !is KMutableProperty<*>)
                    throw IllegalArgumentException("Field ${it.name} is not mutable")
                ProtoFieldDescriptor(it)
            }.sorted()

    val descriptorsAsMap = descriptors.associateBy { it.fieldNumber }

    val instanceFactory: () -> T

    init {
        val constructor = kClass.constructors.firstOrNull()
            ?: throw IllegalArgumentException("No constructor found for class: $kClass")
        val paramsMap: Map<KParameter, () -> Any?> = constructor.parameters
            .associateWith { it ->
                if (it.type.isMarkedNullable) {
                    return@associateWith { null }
                }
                else {
                    return@associateWith when (it.type.classifier) {
                        String::class -> ({ "" })
                        Int::class -> ({ 0 })
                        Long::class -> ({ 0L })
                        Float::class -> ({ 0f })
                        Double::class -> ({ 0.0 })
                        Boolean::class -> ({ false })
                        ByteArray::class -> ({ ByteArray(0) })
                        List::class -> ({ mutableListOf<Any>() })
                        Map::class -> ({ mutableMapOf<Any, Any>() })
                        else -> {
                            val cls = it.type.classifier as? KClass<*>
                            if (cls != null && cls.isSubclassOf(ProtoMessage::class)) {
                                @Suppress("UNCHECKED_CAST")
                                Global.getProtoModel(cls as KClass<ProtoMessage>).instanceFactory
                            } else {
                                throw IllegalArgumentException("Unsupported type: ${it.type}")
                            }
                        }
                    }
                }
            }
        instanceFactory = {
            val args = paramsMap.mapValues { it.value() } // create default values
            constructor.callBy(args)
        }
    }

    internal fun calculateSize(message: T): Int {
        if (message.packSize > 0)
            return message.packSize // cached size

        descriptors.forEach {
            val value = it.getter.invoke(message)
            if (value == null) return@forEach
            message.packSize += it.serializer
                .unsafeCalculateFullSize(it.tagSize, message, value)
        }
        return message.packSize
    }

    internal fun write(writer: CodedWriter, message: ProtoMessage) {
        descriptors.forEach {
            val value = it.getter.invoke(message)
            if (value == null) return@forEach
            it.serializer
                .unsafeWrite(it.writeTag, writer, message, value)
        }
    }

    internal fun read(reader: CodedReader, length: Int): T {
        val message = instanceFactory()
        val end = reader.bytesRead + length
        while (reader.bytesRead < end) {
            val (fieldNumber, wireType) = reader.readTag()
            val descriptor = descriptorsAsMap[fieldNumber]
            if (descriptor != null)
                descriptor.deserializer.deserialize(
                    reader,
                    wireType,
                    message,
                    descriptor.getter,
                    descriptor.setter
                )
            else
                reader.skipField(wireType)
        }
        return message
    }
}