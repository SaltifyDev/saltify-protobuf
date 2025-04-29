@file:Suppress("UNCHECKED_CAST")

package org.ntqqrev.saltify.protobuf.deserializer

import org.ntqqrev.saltify.protobuf.Global
import org.ntqqrev.saltify.protobuf.ProtoMessage
import org.ntqqrev.saltify.protobuf.model.WireType
import org.ntqqrev.saltify.protobuf.util.CodedReader
import java.lang.invoke.MethodHandle
import kotlin.reflect.KClass

internal object ByteArrayDeserializer : ProtoFieldDeserializer {
    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: Any,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val value = when (wireType) {
            WireType.LENGTH_DELIMITED.value -> reader.readLengthDelimited()
            else -> throw IllegalArgumentException("Invalid wire type for ByteArray: $wireType")
        }
        setter.invoke(message, value)
    }
}

internal object ByteArrayRepeatedDeserializer : ProtoFieldDeserializer {
    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: Any,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val list = getter.invoke(message) as MutableList<ByteArray>
        when (wireType) {
            WireType.LENGTH_DELIMITED.value -> {
                val value = reader.readLengthDelimited()
                list.add(value)
            }

            else -> throw IllegalArgumentException("Invalid wire type for ByteArray: $wireType")
        }
    }
}

internal object StringDeserializer : ProtoFieldDeserializer {
    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: Any,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val value = when (wireType) {
            WireType.LENGTH_DELIMITED.value -> reader.readLengthDelimited()
            else -> throw IllegalArgumentException("Invalid wire type for String: $wireType")
        }
        setter.invoke(message, value.decodeToString())
    }
}

internal object StringRepeatedDeserializer : ProtoFieldDeserializer {
    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: Any,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val list = getter.invoke(message) as MutableList<String>
        when (wireType) {
            WireType.LENGTH_DELIMITED.value -> {
                val value = reader.readLengthDelimited()
                list.add(value.decodeToString())
            }

            else -> throw IllegalArgumentException("Invalid wire type for String: $wireType")
        }
    }
}

internal class ProtoMessageDeserializer(val kClass: KClass<out ProtoMessage>) : ProtoFieldDeserializer {
    private val model by lazy { Global.getProtoModel(kClass) }

    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: Any,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val size = when (wireType) {
            WireType.LENGTH_DELIMITED.value -> reader.readVarint32()
            else -> throw IllegalArgumentException("Invalid wire type for ProtoMessage: $wireType")
        }
        val value = model.read(reader, size)
        setter.invoke(message, value)
    }
}

internal class ProtoMessageRepeatedDeserializer(val kClass: KClass<out ProtoMessage>) : ProtoFieldDeserializer {
    private val model by lazy { Global.getProtoModel(kClass) }

    override fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: Any,
        getter: MethodHandle,
        setter: MethodHandle
    ) {
        val list = getter.invoke(message) as MutableList<ProtoMessage>
        when (wireType) {
            WireType.LENGTH_DELIMITED.value -> {
                val size = reader.readVarint32()
                val value = model.read(reader, size)
                list.add(value)
            }

            else -> throw IllegalArgumentException("Invalid wire type for ProtoMessage: $wireType")
        }
    }
}