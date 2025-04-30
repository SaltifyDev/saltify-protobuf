@file:Suppress("UNCHECKED_CAST")

package org.ntqqrev.saltify.protobuf.deserializer

import org.ntqqrev.saltify.protobuf.ProtoMessage
import org.ntqqrev.saltify.protobuf.model.WireType
import org.ntqqrev.saltify.protobuf.util.CodedReader
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

internal class MapDeserializer<K : Any, V : Any>(
    val keyClass: KClass<K>,
    val valueClass: KClass<V>,
) : ProtoFieldDeserializer {
    val keyDeserializer = when (keyClass) {
        String::class -> StringDeserializer
        Int::class -> IntVarintDeserializer
        Long::class -> LongVarintDeserializer
        Boolean::class -> BooleanDeserializer
        else -> throw IllegalArgumentException("Unsupported key type: $keyClass")
    }

    val valueDeserializer = when (valueClass) {
        String::class -> StringDeserializer
        Int::class -> IntVarintDeserializer
        Long::class -> LongVarintDeserializer
        Float::class -> FloatDeserializer
        Double::class -> DoubleDeserializer
        Boolean::class -> BooleanDeserializer
        ByteArray::class -> ByteArrayDeserializer
        else -> {
            if (!valueClass.isSubclassOf(ProtoMessage::class)) {
                throw IllegalArgumentException("Unsupported value type: $valueClass")
            }
            ProtoMessageDeserializer(valueClass as KClass<ProtoMessage>)
        }
    }

    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: Any,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val map = getter.invoke(message) as MutableMap<K, V>
        when (wireType) {
            WireType.LENGTH_DELIMITED.value -> {
                val count = reader.readVarint32()
                val end = reader.bytesRead + count
                val entry = Entry()
                while (reader.bytesRead < end) {
                    val (fieldNumber, wireType) = reader.readTag()
                    when (fieldNumber) {
                        1 -> keyDeserializer.deserialize(
                            reader, wireType, entry,
                            entryKeyGetter, entryKeySetter
                        )
                        2 -> valueDeserializer.deserialize(
                            reader, wireType, entry,
                            entryValueGetter, entryValueSetter
                        )
                    }
                }
                if (entry.key != null && entry.value != null) {
                    map[entry.key as K] = entry.value as V
                } else {
                    throw IllegalArgumentException("Missing key or value in map entry")
                }
            }

            else -> throw IllegalArgumentException("Invalid wire type for Map: $wireType")
        }
    }

    class Entry(
        var key: Any? = null,
        var value: Any? = null,
    )

    companion object {
        private val lookup = MethodHandles.lookup()
        val entryKeyGetter: MethodHandle = lookup.findVirtual(
            Entry::class.java,
            "getKey",
            MethodType.methodType(Any::class.java)
        )
        val entryValueGetter: MethodHandle = lookup.findVirtual(
            Entry::class.java,
            "getValue",
            MethodType.methodType(Any::class.java)
        )
        val entryKeySetter: MethodHandle = lookup.findVirtual(
            Entry::class.java,
            "setKey",
            MethodType.methodType(Void.TYPE, Any::class.java)
        )
        val entryValueSetter: MethodHandle = lookup.findVirtual(
            Entry::class.java,
            "setValue",
            MethodType.methodType(Void.TYPE, Any::class.java)
        )
    }
}