package org.ntqqrev.saltify.protobuf.serializer

import org.ntqqrev.saltify.protobuf.ProtoMessage
import org.ntqqrev.saltify.protobuf.util.CodedWriter

internal object BooleanSerializer : ProtoFieldSerializer<Boolean> {
    override fun calculateFullSize(
        tagSize: Int,
        message: ProtoMessage,
        value: Boolean
    ): Int = 1 + tagSize

    override fun write(
        tag: Int,
        writer: CodedWriter,
        message: ProtoMessage,
        value: Boolean
    ) {
        writer.writeVarint(tag)
        writer.writeBoolean(value)
    }
}

internal object BooleanPackedSerializer : PackedFieldSerializer<Boolean>() {
    override fun calculateBodySize(
        message: ProtoMessage,
        value: List<Boolean>
    ): Int = value.size

    override fun writeSingle(
        writer: CodedWriter,
        message: ProtoMessage,
        value: Boolean
    ) {
        writer.writeBoolean(value)
    }
}

internal object BooleanNotPackedSerializer : NotPackedFieldSerializer<Boolean>() {
    override fun calculateBodySize(
        message: ProtoMessage,
        value: List<Boolean>
    ): Int = value.size

    override fun writeSingle(
        writer: CodedWriter,
        message: ProtoMessage,
        value: Boolean
    ) {
        writer.writeBoolean(value)
    }
}