@file:Suppress("UNCHECKED_CAST")

package org.ntqqrev.saltify.protobuf.serializer

import org.ntqqrev.saltify.protobuf.ProtoMessage
import org.ntqqrev.saltify.protobuf.util.CodedWriter
import org.ntqqrev.saltify.protobuf.util.varintSize

internal abstract class PackedFieldSerializer<T> : ProtoFieldSerializer<List<T>> {
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
        val bodySize = calculateBodySize(message, value)
        message.cachedSize[value] = bodySize
        return bodySize + tagSize + bodySize.varintSize()
    }

    override fun write(
        tag: Int,
        writer: CodedWriter,
        message: ProtoMessage,
        value: List<T>
    ) {
        writer.writeVarint(tag)
        writer.writeVarint(message.cachedSize[value]!!)
        value.forEach { writeSingle(writer, message, it) }
    }
}