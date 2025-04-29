@file:Suppress("UNCHECKED_CAST")

package org.ntqqrev.saltify.protobuf.deserializer

import org.ntqqrev.saltify.protobuf.model.WireType
import org.ntqqrev.saltify.protobuf.util.CodedReader
import java.lang.invoke.MethodHandle

internal object FloatDeserializer : ProtoFieldDeserializer {
    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: Any,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val value = when (wireType) {
            WireType.FIXED32.value -> reader.readFloat()
            else -> throw IllegalArgumentException("Invalid wire type for Float: $wireType")
        }
        setter.invoke(message, value)
    }
}

internal object FloatRepeatedDeserializer : ProtoFieldDeserializer {
    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: Any,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val list = getter.invoke(message) as MutableList<Float>
        when (wireType) {
            WireType.FIXED32.value -> {
                val value = reader.readFloat()
                list.add(value)
            }

            WireType.LENGTH_DELIMITED.value -> {
                val count = reader.readVarint32() / 4
                repeat(count) {
                    val value = reader.readFloat()
                    list.add(value)
                }
            }

            else -> throw IllegalArgumentException("Invalid wire type for Float: $wireType")
        }
    }
}

internal object DoubleDeserializer : ProtoFieldDeserializer {
    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: Any,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val value = when (wireType) {
            WireType.FIXED64.value -> reader.readDouble()
            else -> throw IllegalArgumentException("Invalid wire type for Double: $wireType")
        }
        setter.invoke(message, value)
    }
}

internal object DoubleRepeatedDeserializer : ProtoFieldDeserializer {
    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: Any,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val list = getter.invoke(message) as MutableList<Double>
        when (wireType) {
            WireType.FIXED64.value -> {
                val value = reader.readDouble()
                list.add(value)
            }

            WireType.LENGTH_DELIMITED.value -> {
                val count = reader.readVarint32() / 8
                repeat(count) {
                    val value = reader.readDouble()
                    list.add(value)
                }
            }

            else -> throw IllegalArgumentException("Invalid wire type for Double: $wireType")
        }
    }
}