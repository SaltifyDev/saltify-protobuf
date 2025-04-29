package org.ntqqrev.saltify.protobuf.serializer

import org.ntqqrev.saltify.protobuf.ProtoMessage
import org.ntqqrev.saltify.protobuf.util.CodedWriter

internal interface ProtoFieldSerializer<T> {
    fun calculateFullSize(
        tagSize: Int,
        message: ProtoMessage,
        value: T
    ): Int

    fun write(
        tag: Int,
        writer: CodedWriter,
        message: ProtoMessage,
        value: T
    )

    @Suppress("UNCHECKED_CAST")
    fun unsafeCalculateFullSize(
        tagSize: Int,
        message: ProtoMessage,
        value: Any
    ): Int {
        val s = calculateFullSize(tagSize, message, value as T)
        return s
    }

    @Suppress("UNCHECKED_CAST")
    fun unsafeWrite(
        tag: Int,
        writer: CodedWriter,
        message: ProtoMessage,
        value: Any
    ) {
        write(tag, writer, message, value as T)
    }
}