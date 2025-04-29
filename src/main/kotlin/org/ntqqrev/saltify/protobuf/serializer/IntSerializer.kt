package org.ntqqrev.saltify.protobuf.serializer

import org.ntqqrev.saltify.protobuf.ProtoMessage
import org.ntqqrev.saltify.protobuf.util.CodedWriter
import org.ntqqrev.saltify.protobuf.util.varint32Size
import org.ntqqrev.saltify.protobuf.util.varintSize
import org.ntqqrev.saltify.protobuf.util.zigzag

internal object IntVarintSerializer : ProtoFieldSerializer<Int> {
    override fun calculateFullSize(
        tagSize: Int,
        message: ProtoMessage,
        value: Int
    ): Int = value.varintSize() + tagSize

    override fun write(
        tag: Int,
        writer: CodedWriter,
        message: ProtoMessage,
        value: Int
    ) {
        writer.writeVarint(tag)
        writer.writeVarint(value)
    }
}

internal object IntFixed32Serializer : ProtoFieldSerializer<Int> {
    override fun calculateFullSize(
        tagSize: Int,
        message: ProtoMessage,
        value: Int
    ): Int = 4 + tagSize

    override fun write(
        tag: Int,
        writer: CodedWriter,
        message: ProtoMessage,
        value: Int
    ) {
        writer.writeVarint(tag)
        writer.writeFixed(value)
    }
}

internal object IntPackedVarintSerializer : PackedFieldSerializer<Int>() {
    override fun calculateBodySize(
        message: ProtoMessage,
        value: List<Int>
    ): Int = value.varint32Size()

    override fun writeSingle(
        writer: CodedWriter,
        message: ProtoMessage,
        value: Int
    ) {
        writer.writeVarint(value)
    }
}

internal object IntPackedFixed32Serializer : PackedFieldSerializer<Int>() {
    override fun calculateBodySize(
        message: ProtoMessage,
        value: List<Int>
    ): Int = value.size * 4

    override fun writeSingle(
        writer: CodedWriter,
        message: ProtoMessage,
        value: Int
    ) {
        writer.writeFixed(value)
    }
}

internal object IntNotPackedVarintSerializer : NotPackedFieldSerializer<Int>() {
    override fun calculateBodySize(
        message: ProtoMessage,
        value: List<Int>
    ): Int = value.varint32Size()

    override fun writeSingle(
        writer: CodedWriter,
        message: ProtoMessage,
        value: Int
    ) {
        writer.writeVarint(value)
    }
}

internal object IntNotPackedFixed32Serializer : NotPackedFieldSerializer<Int>() {
    override fun calculateBodySize(
        message: ProtoMessage,
        value: List<Int>
    ): Int = value.size * 4

    override fun writeSingle(
        writer: CodedWriter,
        message: ProtoMessage,
        value: Int
    ) {
        writer.writeFixed(value)
    }
}

internal object IntZigzagVarintSerializer : ProtoFieldSerializer<Int> {
    override fun calculateFullSize(
        tagSize: Int,
        message: ProtoMessage,
        value: Int
    ): Int = value.zigzag().varintSize() + tagSize

    override fun write(
        tag: Int,
        writer: CodedWriter,
        message: ProtoMessage,
        value: Int
    ) {
        writer.writeVarint(tag)
        writer.writeVarint(value.zigzag())
    }
}

internal object IntZigzagFixed32Serializer : ProtoFieldSerializer<Int> {
    override fun calculateFullSize(
        tagSize: Int,
        message: ProtoMessage,
        value: Int
    ): Int = 4 + tagSize

    override fun write(
        tag: Int,
        writer: CodedWriter,
        message: ProtoMessage,
        value: Int
    ) {
        writer.writeVarint(tag)
        writer.writeFixed(value.zigzag())
    }
}

internal object IntZigzagPackedVarintSerializer : PackedFieldSerializer<Int>() {
    override fun calculateBodySize(
        message: ProtoMessage,
        value: List<Int>
    ): Int = value.map { it.zigzag() }.varint32Size()

    override fun writeSingle(
        writer: CodedWriter,
        message: ProtoMessage,
        value: Int
    ) {
        writer.writeVarint(value.zigzag())
    }
}

internal object IntZigzagPackedFixed32Serializer : PackedFieldSerializer<Int>() {
    override fun calculateBodySize(
        message: ProtoMessage,
        value: List<Int>
    ): Int = value.size * 4

    override fun writeSingle(
        writer: CodedWriter,
        message: ProtoMessage,
        value: Int
    ) {
        writer.writeFixed(value.zigzag())
    }
}

internal object IntZigzagNotPackedVarintSerializer : NotPackedFieldSerializer<Int>() {
    override fun calculateBodySize(
        message: ProtoMessage,
        value: List<Int>
    ): Int = value.map { it.zigzag() }.varint32Size()

    override fun writeSingle(
        writer: CodedWriter,
        message: ProtoMessage,
        value: Int
    ) {
        writer.writeVarint(value.zigzag())
    }
}

internal object IntZigzagNotPackedFixed32Serializer : NotPackedFieldSerializer<Int>() {
    override fun calculateBodySize(
        message: ProtoMessage,
        value: List<Int>
    ): Int = value.size * 4

    override fun writeSingle(
        writer: CodedWriter,
        message: ProtoMessage,
        value: Int
    ) {
        writer.writeFixed(value.zigzag())
    }
}