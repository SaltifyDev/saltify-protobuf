@file:Suppress("UNCHECKED_CAST")

package org.ntqqrev.saltify.protobuf.serializer

import org.ntqqrev.saltify.protobuf.ProtoMessage
import org.ntqqrev.saltify.protobuf.util.CodedWriter

internal abstract class NotPackedFieldSerializer<T> : ProtoFieldSerializer<List<T>> {
    abstract fun calculateBodySize(
        message: ProtoMessage,
        value: List<T>
    ): Int

    abstract fun writeSingle(
        writer: CodedWriter,
        message: ProtoMessage,
        value: T
    )

    override fun calculateFullSize(
        tagSize: Int,
        message: ProtoMessage,
        value: List<T>
    ): Int {
        return calculateBodySize(message, value) + tagSize * value.size
    }

    override fun write(
        tag: Int,
        writer: CodedWriter,
        message: ProtoMessage,
        value: List<T>
    ) {
        value.forEach {
            writer.writeVarint(tag)
            writeSingle(writer, message, it)
        }
    }
}