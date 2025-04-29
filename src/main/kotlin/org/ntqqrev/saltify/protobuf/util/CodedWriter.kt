package org.ntqqrev.saltify.protobuf.util

internal abstract class CodedWriter {
    abstract fun writeByte(value: Byte)

    fun writeVarint(value: Int) {
        var v = value
        while (v and 0xFFFFFF80.toInt() != 0) {
            writeByte((v and 0x7F or 0x80).toByte())
            v = v ushr 7
        }
        writeByte(v.toByte())
    }

    fun writeFixed(value: Int) {
        writeByte((value and 0xFF).toByte())
        writeByte((value shr 8 and 0xFF).toByte())
        writeByte((value shr 16 and 0xFF).toByte())
        writeByte((value shr 24 and 0xFF).toByte())
    }

    fun writeVarint(value: Long) {
        var v = value
        while (v and 0xFFFFFF80 != 0L) {
            writeByte((v and 0x7F or 0x80).toByte())
            v = v ushr 7
        }
        writeByte(v.toByte())
    }

    fun writeFixed(value: Long) {
        writeByte((value and 0xFF).toByte())
        writeByte((value shr 8 and 0xFF).toByte())
        writeByte((value shr 16 and 0xFF).toByte())
        writeByte((value shr 24 and 0xFF).toByte())
        writeByte((value shr 32 and 0xFF).toByte())
        writeByte((value shr 40 and 0xFF).toByte())
        writeByte((value shr 48 and 0xFF).toByte())
        writeByte((value shr 56 and 0xFF).toByte())
    }

    fun writeFixed(value: Float) {
        writeFixed(value.toBits())
    }

    fun writeFixed(value: Double) {
        writeFixed(value.toBits())
    }

    fun writeBoolean(value: Boolean) {
        writeByte(if (value) 1 else 0)
    }

    fun writeLengthDelimited(value: ByteArray) {
        writeVarint(value.size)
        value.forEach { writeByte(it) }
    }

    abstract fun build(): ByteArray
}