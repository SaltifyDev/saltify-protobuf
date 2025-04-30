package org.ntqqrev.saltify.protobuf.annotation

/**
 * This annotation is used to disable packing for a field in a ProtoMessage.
 * By default, fields are packed when serialized.
 * This annotation can be used to prevent packing for specific fields.
 * These fields include:
 * - All repeated integer fields
 * - All repeated boolean fields
 * - All repeated float/double fields
 *
 * Deserialization behavior is not affected by this annotation.
 */
@Target(AnnotationTarget.FIELD)
annotation class DisablePacking