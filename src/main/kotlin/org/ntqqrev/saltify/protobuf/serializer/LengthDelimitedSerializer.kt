package org.ntqqrev.saltify.protobuf.serializer

import org.ntqqrev.saltify.protobuf.Global
import org.ntqqrev.saltify.protobuf.ProtoMessage
import org.ntqqrev.saltify.protobuf.util.CodedWriter
import org.ntqqrev.saltify.protobuf.util.varintSize
import kotlin.reflect.KClass

internal object ByteArraySerializer : ProtoFieldSerializer<ByteArray> {
    override fun calculateFullSize(
        tagSize: Int,
        message: ProtoMessage,
        value: ByteArray
    ): Int {
        val bodySize = value.size
        return bodySize + tagSize + bodySize.varintSize()
    }

    override fun write(
        tag: Int,
        writer: CodedWriter,
        message: ProtoMessage,
        value: ByteArray
    ) {
        writer.writeVarint(tag)
        writer.writeLengthDelimited(value)
    }
}

internal object ByteArrayRepeatedSerializer : NotPackedFieldSerializer<ByteArray>() {
    override fun calculateBodySize(
        message: ProtoMessage,
        value: List<ByteArray>
    ): Int = value.fold(0) { acc, v -> acc + v.size + v.size.varintSize() }

    override fun writeSingle(
        writer: CodedWriter,
        message: ProtoMessage,
        value: ByteArray
    ) {
        writer.writeLengthDelimited(value)
    }
}

internal object StringSerializer : ProtoFieldSerializer<String> {
    override fun calculateFullSize(
        tagSize: Int,
        message: ProtoMessage,
        value: String
    ): Int {
        val byteArray = value.toByteArray()
        message.cachedString2ByteArray[value] = byteArray
        val bodySize = byteArray.size
        return bodySize + tagSize + bodySize.varintSize()
    }

    override fun write(
        tag: Int,
        writer: CodedWriter,
        message: ProtoMessage,
        value: String
    ) {
        val byteArray = message.cachedString2ByteArray[value]!!
        writer.writeVarint(tag)
        writer.writeLengthDelimited(byteArray)
    }
}

internal object StringRepeatedSerializer : NotPackedFieldSerializer<String>() {
    override fun calculateBodySize(
        message: ProtoMessage,
        value: List<String>
    ): Int {
        var size = 0
        value.forEach {
            val byteArray = it.toByteArray()
            message.cachedString2ByteArray[it] = byteArray
            size += byteArray.size + byteArray.size.varintSize()
        }
        return size
    }

    override fun writeSingle(
        writer: CodedWriter,
        message: ProtoMessage,
        value: String
    ) {
        val byteArray = message.cachedString2ByteArray[value]!!
        writer.writeLengthDelimited(byteArray)
    }
}

internal class ProtoMessageSerializer<T : ProtoMessage>(kClass: KClass<T>) : ProtoFieldSerializer<T> {
    private val model by lazy { Global.getProtoModel(kClass) }

    override fun calculateFullSize(
        tagSize: Int,
        message: ProtoMessage,
        value: T
    ): Int {
        val bodySize = model.calculateSize(value)
        return bodySize + tagSize + bodySize.varintSize()
    }

    override fun write(
        tag: Int,
        writer: CodedWriter,
        message: ProtoMessage,
        value: T
    ) {
        writer.writeVarint(tag)
        writer.writeVarint(value.packSize)
        model.write(writer, value)
    }
}

internal class ProtoMessageRepeatedSerializer<T : ProtoMessage>(kClass: KClass<T>) :
    NotPackedFieldSerializer<T>() {
    private val model by lazy { Global.getProtoModel(kClass) }

    override fun calculateBodySize(
        message: ProtoMessage,
        value: List<T>
    ): Int = value.fold(0) { acc, v ->
        val size = model.calculateSize(v)
        acc + size + size.varintSize()
    }

    override fun writeSingle(
        writer: CodedWriter,
        message: ProtoMessage,
        value: T
    ) {
        writer.writeVarint(value.packSize)
        model.write(writer, value)
    }
}