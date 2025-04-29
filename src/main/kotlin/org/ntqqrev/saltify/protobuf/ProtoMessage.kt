package org.ntqqrev.saltify.protobuf

import org.ntqqrev.saltify.protobuf.annotation.ProtoIgnore

open class ProtoMessage {
    @ProtoIgnore
    internal var packSize = 0
    @ProtoIgnore
    internal val cachedSize = mutableMapOf<Any, Int>()
    @ProtoIgnore
    internal val cachedString2ByteArray = mutableMapOf<String, ByteArray>()
}