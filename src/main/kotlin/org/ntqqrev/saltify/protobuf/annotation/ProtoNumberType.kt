package org.ntqqrev.saltify.protobuf.annotation

/**
 * Specifies the number type for a field in a ProtoMessage.
 */
@Target(AnnotationTarget.FIELD)
annotation class ProtoNumberType(val flag: Int)