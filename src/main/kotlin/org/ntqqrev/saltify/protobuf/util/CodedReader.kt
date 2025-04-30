package org.ntqqrev.saltify.protobuf.util

import org.ntqqrev.saltify.protobuf.model.WireType

internal abstract class CodedReader {
    abstract fun readByte(): Byte
    abstract fun skip(bytes: Int)
    abstract val bytesRead: Int

    fun readVarint32(): Int {
        var result = 0
        var shift = 0
        while (true) {
            val byte = readByte()
            result = result or ((byte.toInt() and 0x7F) shl shift)
            if (byte.toInt() and 0x80 == 0) {
                break
            }
            shift += 7
        }
        return result
    }

    fun readFixed32(): Int {
        return (readByte().toInt() and 0xFF) or
                ((readByte().toInt() and 0xFF) shl 8) or
                ((readByte().toInt() and 0xFF) shl 16) or
                ((readByte().toInt() and 0xFF) shl 24)
    }

    fun readVarint64(): Long {
        var result = 0L
        var shift = 0
        while (true) {
            val byte = readByte()
            result = result or ((byte.toLong() and 0x7F) shl shift)
            if (byte.toInt() and 0x80 == 0) {
                break
            }
            shift += 7
        }
        return result
    }

    fun readFixed64(): Long {
        return (readByte().toLong() and 0xFF) or
                ((readByte().toLong() and 0xFF) shl 8) or
                ((readByte().toLong() and 0xFF) shl 16) or
                ((readByte().toLong() and 0xFF) shl 24) or
                ((readByte().toLong() and 0xFF) shl 32) or
                ((readByte().toLong() and 0xFF) shl 40) or
                ((readByte().toLong() and 0xFF) shl 48) or
                ((readByte().toLong() and 0xFF) shl 56)
    }

    fun readFloat(): Float {
        return Float.fromBits(readFixed32())
    }

    fun readDouble(): Double {
        return Double.fromBits(readFixed64())
    }

    fun readBoolean(): Boolean {
        return readByte() != 0.toByte()
    }

    fun readLengthDelimited(): ByteArray {
        val length = readVarint32()
        val result = ByteArray(length)
        for (i in 0 until length) {
            result[i] = readByte()
        }
        return result
    }

    fun readTag(): Pair<Int, Int> {
        val tag = readVarint32()
        val fieldNumber = tag ushr 3
        val wireType = tag and 0x7
        return Pair(fieldNumber, wireType)
    }

    fun skipField(wireType: Int) {
        when (wireType) {
            WireType.VARINT.value -> readVarint32()
            WireType.FIXED64.value -> readFixed64()
            WireType.LENGTH_DELIMITED.value -> {
                val length = readVarint32()
                skip(length)
            }
            // WireType.START_GROUP.value -> throw UnsupportedOperationException("START_GROUP not supported")
            // WireType.END_GROUP.value -> throw UnsupportedOperationException("END_GROUP not supported")
            WireType.FIXED32.value -> readFixed32()
            else -> throw IllegalArgumentException("Invalid wire type: $wireType")
        }
    }
}