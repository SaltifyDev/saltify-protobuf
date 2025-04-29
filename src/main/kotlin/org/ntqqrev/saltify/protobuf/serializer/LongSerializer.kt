package org.ntqqrev.saltify.protobuf.serializer

import org.ntqqrev.saltify.protobuf.ProtoMessage
import org.ntqqrev.saltify.protobuf.util.CodedWriter
import org.ntqqrev.saltify.protobuf.util.varint64Size
import org.ntqqrev.saltify.protobuf.util.varintSize
import org.ntqqrev.saltify.protobuf.util.zigzag

internal object LongVarintSerializer : ProtoFieldSerializer<Long> {
    override fun calculateFullSize(
        tagSize: Int,
        message: ProtoMessage,
        value: Long
    ): Int = value.varintSize() + tagSize

    override fun write(
        tag: Int,
        writer: CodedWriter,
        message: ProtoMessage,
        value: Long
    ) {
        writer.writeVarint(tag)
        writer.writeVarint(value)
    }
}

internal object LongFixed64Serializer : ProtoFieldSerializer<Long> {
    override fun calculateFullSize(
        tagSize: Int,
        message: ProtoMessage,
        value: Long
    ): Int = 8 + tagSize

    override fun write(
        tag: Int,
        writer: CodedWriter,
        message: ProtoMessage,
        value: Long
    ) {
        writer.writeVarint(tag)
        writer.writeFixed(value)
    }
}

internal object LongPackedVarintSerializer : PackedFieldSerializer<Long>() {
    override fun calculateBodySize(
        message: ProtoMessage,
        value: List<Long>
    ): Int = value.varint64Size()

    override fun writeSingle(
        writer: CodedWriter,
        message: ProtoMessage,
        value: Long
    ) {
        writer.writeVarint(value)
    }
}

internal object LongPackedFixed64Serializer : PackedFieldSerializer<Long>() {
    override fun calculateBodySize(
        message: ProtoMessage,
        value: List<Long>
    ): Int = value.size * 8

    override fun writeSingle(
        writer: CodedWriter,
        message: ProtoMessage,
        value: Long
    ) {
        writer.writeFixed(value)
    }
}

internal object LongNotPackedVarintSerializer : NotPackedFieldSerializer<Long>() {
    override fun calculateBodySize(
        message: ProtoMessage,
        value: List<Long>
    ): Int = value.varint64Size()

    override fun writeSingle(
        writer: CodedWriter,
        message: ProtoMessage,
        value: Long
    ) {
        writer.writeVarint(value)
    }
}

internal object LongNotPackedFixed64Serializer : NotPackedFieldSerializer<Long>() {
    override fun calculateBodySize(
        message: ProtoMessage,
        value: List<Long>
    ): Int = value.size * 8

    override fun writeSingle(
        writer: CodedWriter,
        message: ProtoMessage,
        value: Long
    ) {
        writer.writeFixed(value)
    }
}

internal object LongZigzagVarintSerializer : ProtoFieldSerializer<Long> {
    override fun calculateFullSize(
        tagSize: Int,
        message: ProtoMessage,
        value: Long
    ): Int = value.zigzag().varintSize() + tagSize

    override fun write(
        tag: Int,
        writer: CodedWriter,
        message: ProtoMessage,
        value: Long
    ) {
        writer.writeVarint(tag)
        writer.writeVarint(value.zigzag())
    }
}

internal object LongZigzagFixed64Serializer : ProtoFieldSerializer<Long> {
    override fun calculateFullSize(
        tagSize: Int,
        message: ProtoMessage,
        value: Long
    ): Int = 8 + tagSize

    override fun write(
        tag: Int,
        writer: CodedWriter,
        message: ProtoMessage,
        value: Long
    ) {
        writer.writeVarint(tag)
        writer.writeFixed(value.zigzag())
    }
}

internal object LongZigzagPackedVarintSerializer : PackedFieldSerializer<Long>() {
    override fun calculateBodySize(
        message: ProtoMessage,
        value: List<Long>
    ): Int = value.fold(0) { acc, v -> acc + v.zigzag().varintSize() }

    override fun writeSingle(
        writer: CodedWriter,
        message: ProtoMessage,
        value: Long
    ) {
        writer.writeVarint(value.zigzag())
    }
}

internal object LongZigzagPackedFixed64Serializer : PackedFieldSerializer<Long>() {
    override fun calculateBodySize(
        message: ProtoMessage,
        value: List<Long>
    ): Int = value.size * 8

    override fun writeSingle(
        writer: CodedWriter,
        message: ProtoMessage,
        value: Long
    ) {
        writer.writeFixed(value.zigzag())
    }
}

internal object LongZigzagNotPackedVarintSerializer : NotPackedFieldSerializer<Long>() {
    override fun calculateBodySize(
        message: ProtoMessage,
        value: List<Long>
    ): Int = value.fold(0) { acc, v -> acc + v.zigzag().varintSize() }

    override fun writeSingle(
        writer: CodedWriter,
        message: ProtoMessage,
        value: Long
    ) {
        writer.writeVarint(value.zigzag())
    }
}

internal object LongZigzagNotPackedFixed64Serializer : NotPackedFieldSerializer<Long>() {
    override fun calculateBodySize(
        message: ProtoMessage,
        value: List<Long>
    ): Int = value.size * 8

    override fun writeSingle(
        writer: CodedWriter,
        message: ProtoMessage,
        value: Long
    ) {
        writer.writeFixed(value.zigzag())
    }
}