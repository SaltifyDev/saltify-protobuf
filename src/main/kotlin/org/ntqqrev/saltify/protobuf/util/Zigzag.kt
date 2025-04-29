package org.ntqqrev.saltify.protobuf.util

fun Int.zigzag(): Int {
    return (this shl 1) xor (this shr 31)
}

fun Long.zigzag(): Long {
    return (this shl 1) xor (this shr 63)
}

fun Int.unzigzag(): Int {
    return (this ushr 1) xor -(this and 1)
}

fun Long.unzigzag(): Long {
    return (this ushr 1) xor -(this and 1)
}