@file:Suppress("UNCHECKED_CAST")

package org.ntqqrev.saltify.protobuf.deserializer

import org.ntqqrev.saltify.protobuf.model.WireType
import org.ntqqrev.saltify.protobuf.util.CodedReader
import java.lang.invoke.MethodHandle

internal object BooleanDeserializer : ProtoFieldDeserializer {
    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: Any,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val value = when (wireType) {
            WireType.VARINT.value -> reader.readVarint32() != 0
            else -> throw IllegalArgumentException("Invalid wire type for Boolean: $wireType")
        }
        setter.invoke(message, value)
    }
}

internal object BooleanRepeatedDeserializer : ProtoFieldDeserializer {
    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: Any,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val list = getter.invoke(message) as MutableList<Boolean>
        when (wireType) {
            WireType.VARINT.value -> {
                val value = reader.readBoolean()
                list.add(value)
            }

            WireType.LENGTH_DELIMITED.value -> {
                val count = reader.readVarint32()
                repeat(count) {
                    val value = reader.readBoolean()
                    list.add(value)
                }
            }

            else -> throw IllegalArgumentException("Invalid wire type for Boolean: $wireType")
        }
    }
}