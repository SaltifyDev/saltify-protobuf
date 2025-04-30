package org.ntqqrev.saltify.protobuf

import org.ntqqrev.saltify.protobuf.annotation.ProtoIgnore

/**
 * Base class for ProtoMessage.
 * Inherit this class to create your own ProtoMessage.
 */
open class ProtoMessage {
    @ProtoIgnore
    internal var packSize = 0
    @ProtoIgnore
    internal val cachedSize = mutableMapOf<Any, Int>()
    @ProtoIgnore
    internal val cachedString2ByteArray = mutableMapOf<String, ByteArray>()
}