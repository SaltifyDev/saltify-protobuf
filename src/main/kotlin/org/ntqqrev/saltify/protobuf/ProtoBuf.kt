package org.ntqqrev.saltify.protobuf

import org.ntqqrev.saltify.protobuf.util.ByteArrayCodedWriter
import kotlin.reflect.KClass

object ProtoBuf {
    inline fun <reified T : ProtoMessage> serialize(value: T) = serialize(T::class, value)

    fun <T : ProtoMessage> serialize(kClass: KClass<T>, value: T): ByteArray {
        val model = Global.getProtoModel(kClass)
        val size = model.calculateSize(value)
        val writer = ByteArrayCodedWriter(size)
        model.write(writer, value)
        return writer.build()
    }
}