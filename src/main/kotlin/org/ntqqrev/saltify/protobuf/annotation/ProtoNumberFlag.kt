package org.ntqqrev.saltify.protobuf.annotation

/**
 * Flags for `@ProtoNumberType`.
 * @see ProtoNumberType
 */
object ProtoNumberFlag {
    const val VARINT = 0b0000
    const val FIXED = 0b0001
    const val SIGNED = 0b0010
}