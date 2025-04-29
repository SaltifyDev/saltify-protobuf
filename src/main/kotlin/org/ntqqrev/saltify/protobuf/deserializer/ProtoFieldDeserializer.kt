package org.ntqqrev.saltify.protobuf.deserializer

import org.ntqqrev.saltify.protobuf.ProtoMessage
import org.ntqqrev.saltify.protobuf.util.CodedReader
import java.lang.invoke.MethodHandle

internal interface ProtoFieldDeserializer {
    fun deserialize(
        reader: CodedReader,
        wireType: Int,
        message: ProtoMessage,
        getter: MethodHandle,
        setter: MethodHandle,
    )
}