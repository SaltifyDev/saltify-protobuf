package org.ntqqrev.saltify.protobuf.serializer

import org.ntqqrev.saltify.protobuf.ProtoMessage
import org.ntqqrev.saltify.protobuf.util.CodedWriter

internal object FloatSerializer : ProtoFieldSerializer<Float> {
    override fun calculateFullSize(
        tagSize: Int,
        message: ProtoMessage,
        value: Float
    ): Int = 4 + tagSize

    override fun write(
        tag: Int,
        writer: CodedWriter,
        message: ProtoMessage,
        value: Float
    ) {
        writer.writeVarint(tag)
        writer.writeFixed(value)
    }
}

internal object FloatPackedSerializer : PackedFieldSerializer<Float>() {
    override fun calculateBodySize(
        message: ProtoMessage,
        value: List<Float>
    ): Int = value.size * 4

    override fun writeSingle(
        writer: CodedWriter,
        message: ProtoMessage,
        value: Float
    ) {
        writer.writeFixed(value)
    }
}

internal object FloatNotPackedSerializer : NotPackedFieldSerializer<Float>() {
    override fun calculateBodySize(
        message: ProtoMessage,
        value: List<Float>
    ): Int = value.size * 4

    override fun writeSingle(
        writer: CodedWriter,
        message: ProtoMessage,
        value: Float
    ) {
        writer.writeFixed(value)
    }
}

internal object DoubleSerializer : ProtoFieldSerializer<Double> {
    override fun calculateFullSize(
        tagSize: Int,
        message: ProtoMessage,
        value: Double
    ): Int = 8 + tagSize

    override fun write(
        tag: Int,
        writer: CodedWriter,
        message: ProtoMessage,
        value: Double
    ) {
        writer.writeVarint(tag)
        writer.writeFixed(value)
    }
}

internal object DoublePackedSerializer : PackedFieldSerializer<Double>() {
    override fun calculateBodySize(
        message: ProtoMessage,
        value: List<Double>
    ): Int = value.size * 8

    override fun writeSingle(
        writer: CodedWriter,
        message: ProtoMessage,
        value: Double
    ) {
        writer.writeFixed(value)
    }
}

internal object DoubleNotPackedSerializer : NotPackedFieldSerializer<Double>() {
    override fun calculateBodySize(
        message: ProtoMessage,
        value: List<Double>
    ): Int = value.size * 8

    override fun writeSingle(
        writer: CodedWriter,
        message: ProtoMessage,
        value: Double
    ) {
        writer.writeFixed(value)
    }
}