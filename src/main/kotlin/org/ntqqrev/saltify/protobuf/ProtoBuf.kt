package org.ntqqrev.saltify.protobuf

import org.ntqqrev.saltify.protobuf.util.ByteArrayCodedReader
import org.ntqqrev.saltify.protobuf.util.ByteArrayCodedWriter
import kotlin.reflect.KClass

object ProtoBuf {
    /**
     * Serializes a ProtoMessage to a byte array.
     */
    inline fun <reified T : ProtoMessage> serialize(value: T) = serialize(T::class, value)

    /**
     * Serializes a ProtoMessage to a byte array.
     */
    fun <T : ProtoMessage> serialize(kClass: KClass<T>, value: T): ByteArray {
        val model = Global.getProtoModel(kClass)
        val size = model.calculateSize(value)
        val writer = ByteArrayCodedWriter(size)
        model.write(writer, value)
        return writer.build()
    }

    /**
     * Deserializes a byte array to a ProtoMessage.
     */
    inline fun <reified T : ProtoMessage> deserialize(bytes: ByteArray): T =
        deserialize(T::class, bytes)

    /**
     * Deserializes a byte array to a ProtoMessage.
     */
    fun <T : ProtoMessage> deserialize(kClass: KClass<T>, bytes: ByteArray): T {
        val model = Global.getProtoModel(kClass)
        val reader = ByteArrayCodedReader(bytes)
        val message = model.read(reader, bytes.size)
        return message
    }
}