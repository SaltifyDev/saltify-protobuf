package org.ntqqrev.saltify.protobuf

import org.ntqqrev.saltify.protobuf.model.ProtoModel
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

internal object Global {
    private val globalProtoModelCache = ConcurrentHashMap<KClass<*>, ProtoModel<*>>()

    @Suppress("UNCHECKED_CAST")
    fun <T : ProtoMessage> getProtoModel(kClass: KClass<T>): ProtoModel<T> {
        return globalProtoModelCache.getOrPut(kClass) {
            ProtoModel(kClass)
        } as ProtoModel<T>
    }
}