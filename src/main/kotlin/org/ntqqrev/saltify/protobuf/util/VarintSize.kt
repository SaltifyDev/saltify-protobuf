package org.ntqqrev.saltify.protobuf.util

fun Int.varintSize(): Int {
    var v = this
    var size = 0
    while (v and 0xFFFFFF80.toInt() != 0) {
        size++
        v = v ushr 7
    }
    return size + 1
}

fun Long.varintSize(): Int {
    var v = this
    var size = 0
    while (v and 0xFFFFFF80 != 0L) {
        size++
        v = v ushr 7
    }
    return size + 1
}

fun List<Int>.varint32Size(): Int = fold(0) { acc, v -> acc + v.varintSize() }

fun List<Long>.varint64Size(): Int = fold(0) { acc, v -> acc + v.varintSize() }