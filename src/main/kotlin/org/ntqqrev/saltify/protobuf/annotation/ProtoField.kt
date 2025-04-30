package org.ntqqrev.saltify.protobuf.annotation

/**
 * Specifies the field number for a field in a ProtoMessage.
 */
@Target(AnnotationTarget.FIELD)
annotation class ProtoField(val value: Int)
