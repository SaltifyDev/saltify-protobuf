@file:Suppress("UNCHECKED_CAST")

package org.ntqqrev.saltify.protobuf.deserializer

import org.ntqqrev.saltify.protobuf.ProtoMessage
import org.ntqqrev.saltify.protobuf.model.WireType
import org.ntqqrev.saltify.protobuf.util.CodedReader
import org.ntqqrev.saltify.protobuf.util.unzigzag
import java.lang.invoke.MethodHandle

internal object LongVarintDeserializer : ProtoFieldDeserializer {
    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: ProtoMessage,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val value = when (wireType) {
            WireType.VARINT.value -> reader.readVarint64()
            else -> throw IllegalArgumentException("Invalid wire type for Long: $wireType")
        }
        setter.invoke(message, value)
    }
}

internal object LongFixed64Deserializer : ProtoFieldDeserializer {
    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: ProtoMessage,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val value = when (wireType) {
            WireType.FIXED64.value -> reader.readFixed64()
            else -> throw IllegalArgumentException("Invalid wire type for Long: $wireType")
        }
        setter.invoke(message, value)
    }
}

internal object LongZigzagVarintDeserializer : ProtoFieldDeserializer {
    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: ProtoMessage,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val value = when (wireType) {
            WireType.VARINT.value -> reader.readVarint64()
            else -> throw IllegalArgumentException("Invalid wire type for Long: $wireType")
        }
        setter.invoke(message, value.unzigzag())
    }
}

internal object LongZigzagFixed64Deserializer : ProtoFieldDeserializer {
    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: ProtoMessage,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val value = when (wireType) {
            WireType.FIXED64.value -> reader.readFixed64()
            else -> throw IllegalArgumentException("Invalid wire type for Long: $wireType")
        }
        setter.invoke(message, value.unzigzag())
    }
}

internal object LongRepeatedVarintDeserializer : ProtoFieldDeserializer {
    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: ProtoMessage,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val list = getter.invoke(message) as MutableList<Long>
        when (wireType) {
            WireType.VARINT.value -> {
                list.add(reader.readVarint64())
            }

            WireType.LENGTH_DELIMITED.value -> {
                val length = reader.readVarint32()
                val end = reader.bytesRead + length
                while (reader.bytesRead < end) {
                    list.add(reader.readVarint64())
                }
            }

            else -> throw IllegalArgumentException("Invalid wire type for Long: $wireType")
        }
    }
}

internal object LongRepeatedFixed64Deserializer : ProtoFieldDeserializer {
    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: ProtoMessage,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val list = getter.invoke(message) as MutableList<Long>
        when (wireType) {
            WireType.FIXED64.value -> {
                list.add(reader.readFixed64())
            }

            WireType.LENGTH_DELIMITED.value -> {
                val count = reader.readVarint32() / 8
                repeat(count) {
                    list.add(reader.readFixed64())
                }
            }

            else -> throw IllegalArgumentException("Invalid wire type for Long: $wireType")
        }
    }
}

internal object LongRepeatedZigzagVarintDeserializer : ProtoFieldDeserializer {
    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: ProtoMessage,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val list = getter.invoke(message) as MutableList<Long>
        when (wireType) {
            WireType.VARINT.value -> {
                list.add(reader.readVarint64().unzigzag())
            }

            WireType.LENGTH_DELIMITED.value -> {
                val length = reader.readVarint32()
                val end = reader.bytesRead + length
                while (reader.bytesRead < end) {
                    list.add(reader.readVarint64().unzigzag())
                }
            }

            else -> throw IllegalArgumentException("Invalid wire type for Long: $wireType")
        }
    }
}

internal object LongRepeatedZigzagFixed64Deserializer : ProtoFieldDeserializer {
    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: ProtoMessage,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val list = getter.invoke(message) as MutableList<Long>
        when (wireType) {
            WireType.FIXED64.value -> {
                list.add(reader.readFixed64().unzigzag())
            }

            WireType.LENGTH_DELIMITED.value -> {
                val count = reader.readVarint32() / 8
                repeat(count) {
                    list.add(reader.readFixed64().unzigzag())
                }
            }

            else -> throw IllegalArgumentException("Invalid wire type for Long: $wireType")
        }
    }
}