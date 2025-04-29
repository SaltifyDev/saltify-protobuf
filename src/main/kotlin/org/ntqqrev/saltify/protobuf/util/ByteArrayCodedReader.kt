package org.ntqqrev.saltify.protobuf.util

internal class ByteArrayCodedReader(val byteArray: ByteArray) : CodedReader() {
    private var position = 0

    override fun readByte(): Byte {
        return byteArray[position++]
    }

    override val bytesRead: Int
        get() = position
}