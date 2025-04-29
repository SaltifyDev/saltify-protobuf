package org.ntqqrev.saltify.protobuf.util

internal class ByteArrayCodedWriter(size: Int) : CodedWriter() {
    private val buffer = ByteArray(size)
    private var position = 0

    override fun writeByte(value: Byte) {
        buffer[position++] = value
    }

    override fun build(): ByteArray {
        return buffer
    }
}