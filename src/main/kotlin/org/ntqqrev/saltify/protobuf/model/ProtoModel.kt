package org.ntqqrev.saltify.protobuf.model

import org.ntqqrev.saltify.protobuf.ProtoMessage
import org.ntqqrev.saltify.protobuf.annotation.ProtoIgnore
import org.ntqqrev.saltify.protobuf.util.CodedWriter
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaField

internal class ProtoModel<T : ProtoMessage>(kClass: KClass<T>) {
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
}