@file:Suppress("UNCHECKED_CAST")

package org.ntqqrev.saltify.protobuf.deserializer

import org.ntqqrev.saltify.protobuf.model.WireType
import org.ntqqrev.saltify.protobuf.util.CodedReader
import org.ntqqrev.saltify.protobuf.util.unzigzag
import java.lang.invoke.MethodHandle

internal object IntVarintDeserializer : ProtoFieldDeserializer {
    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: Any,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val value = when (wireType) {
            WireType.VARINT.value -> reader.readVarint32()
            else -> throw IllegalArgumentException("Invalid wire type for Int: $wireType")
        }
        setter.invoke(message, value)
    }
}

internal object IntFixed32Deserializer : ProtoFieldDeserializer {
    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: Any,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val value = when (wireType) {
            WireType.FIXED32.value -> reader.readFixed32()
            else -> throw IllegalArgumentException("Invalid wire type for Int: $wireType")
        }
        setter.invoke(message, value)
    }
}

internal object IntZigzagVarintDeserializer : ProtoFieldDeserializer {
    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: Any,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val value = when (wireType) {
            WireType.VARINT.value -> reader.readVarint32()
            else -> throw IllegalArgumentException("Invalid wire type for Int: $wireType")
        }
        setter.invoke(message, value.unzigzag())
    }
}

internal object IntZigzagFixed32Deserializer : ProtoFieldDeserializer {
    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: Any,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val value = when (wireType) {
            WireType.FIXED32.value -> reader.readFixed32()
            else -> throw IllegalArgumentException("Invalid wire type for Int: $wireType")
        }
        setter.invoke(message, value.unzigzag())
    }
}

internal object IntRepeatedVarintDeserializer : ProtoFieldDeserializer {
    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: Any,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val list = getter.invoke(message) as MutableList<Int>
        when (wireType) {
            WireType.VARINT.value -> {
                list.add(reader.readVarint32())
            }

            WireType.LENGTH_DELIMITED.value -> {
                val length = reader.readVarint32()
                val end = reader.bytesRead + length
                while (reader.bytesRead < end) {
                    list.add(reader.readVarint32())
                }
            }

            else -> throw IllegalArgumentException("Invalid wire type for Int: $wireType")
        }
    }
}

internal object IntRepeatedFixed32Deserializer : ProtoFieldDeserializer {
    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: Any,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val list = getter.invoke(message) as MutableList<Int>
        when (wireType) {
            WireType.FIXED32.value -> {
                list.add(reader.readFixed32())
            }

            WireType.LENGTH_DELIMITED.value -> {
                val count = reader.readVarint32() / 4
                repeat(count) {
                    list.add(reader.readFixed32())
                }
            }

            else -> throw IllegalArgumentException("Invalid wire type for Int: $wireType")
        }
    }
}

internal object IntRepeatedZigzagVarintDeserializer : ProtoFieldDeserializer {
    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: Any,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val list = getter.invoke(message) as MutableList<Int>
        when (wireType) {
            WireType.VARINT.value -> {
                list.add(reader.readVarint32().unzigzag())
            }

            WireType.LENGTH_DELIMITED.value -> {
                val length = reader.readVarint32()
                val end = reader.bytesRead + length
                while (reader.bytesRead < end) {
                    list.add(reader.readVarint32().unzigzag())
                }
            }

            else -> throw IllegalArgumentException("Invalid wire type for Int: $wireType")
        }
    }
}

internal object IntRepeatedZigzagFixed32Deserializer : ProtoFieldDeserializer {
    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: Any,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val list = getter.invoke(message) as MutableList<Int>
        when (wireType) {
            WireType.FIXED32.value -> {
                list.add(reader.readFixed32().unzigzag())
            }

            WireType.LENGTH_DELIMITED.value -> {
                val count = reader.readVarint32() / 4
                repeat(count) {
                    list.add(reader.readFixed32().unzigzag())
                }
            }

            else -> throw IllegalArgumentException("Invalid wire type for Int: $wireType")
        }
    }
}