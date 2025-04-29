package org.ntqqrev.saltify.protobuf.test

import org.ntqqrev.saltify.protobuf.ProtoBuf
import org.ntqqrev.saltify.protobuf.ProtoMessage
import org.ntqqrev.saltify.protobuf.annotation.ProtoField

class RecursiveMessage(
    @ProtoField(1)
    var nested: Nested?,

    @ProtoField(2)
    var intField: Int,
) : ProtoMessage() {
    class Nested(
        @ProtoField(1)
        var outer: RecursiveMessage,
    ) : ProtoMessage()
}

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    val message = RecursiveMessage(
        RecursiveMessage.Nested(
            RecursiveMessage(
                null,
                2
            ),
        ),
        1
    )

    val serialized = ProtoBuf.serialize(message)

    println(serialized.toHexString())
}