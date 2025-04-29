package org.ntqqrev.saltify.protobuf.serializer

import org.ntqqrev.saltify.protobuf.ProtoMessage
import org.ntqqrev.saltify.protobuf.model.WireType
import org.ntqqrev.saltify.protobuf.util.CodedWriter
import org.ntqqrev.saltify.protobuf.util.varintSize
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

internal class MapSerializer<K : Any, V : Any>(
    keyClass: KClass<K>,
    valueClass: KClass<V>,
) : ProtoFieldSerializer<Map<K, V>> {
    val keyTag: Int
    val keyTagSize = 1
    val keySerializer: ProtoFieldSerializer<*>
    val valueTag: Int
    val valueTagSize = 1
    val valueSerializer: ProtoFieldSerializer<*>

    init {
        when (keyClass) {
            String::class -> {
                keyTag = 1 shl 3 or WireType.LENGTH_DELIMITED.value
                keySerializer = StringSerializer
            }

            Int::class -> {
                keyTag = 1 shl 3 or WireType.VARINT.value
                keySerializer = IntVarintSerializer
            }

            Long::class -> {
                keyTag = 1 shl 3 or WireType.VARINT.value
                keySerializer = LongVarintSerializer
            }

            Boolean::class -> {
                keyTag = 1 shl 3 or WireType.VARINT.value
                keySerializer = BooleanSerializer
            }

            else -> throw IllegalArgumentException("Unsupported key type: $keyClass")
        }

        when (valueClass) {
            String::class -> {
                valueTag = 2 shl 3 or WireType.LENGTH_DELIMITED.value
                valueSerializer = StringSerializer
            }

            Int::class -> {
                valueTag = 2 shl 3 or WireType.VARINT.value
                valueSerializer = IntVarintSerializer
            }

            Long::class -> {
                valueTag = 2 shl 3 or WireType.VARINT.value
                valueSerializer = LongVarintSerializer
            }

            Float::class -> {
                valueTag = 2 shl 3 or WireType.FIXED32.value
                valueSerializer = FloatSerializer
            }

            Double::class -> {
                valueTag = 2 shl 3 or WireType.FIXED64.value
                valueSerializer = DoubleSerializer
            }

            Boolean::class -> {
                valueTag = 2 shl 3 or WireType.VARINT.value
                valueSerializer = BooleanSerializer
            }

            ByteArray::class -> {
                valueTag = 2 shl 3 or WireType.LENGTH_DELIMITED.value
                valueSerializer = ByteArraySerializer
            }

            else -> {
                if (!valueClass.isSubclassOf(ProtoMessage::class)) {
                    throw IllegalArgumentException("Unsupported value type: $valueClass")
                }
                valueTag = 2 shl 3 or WireType.LENGTH_DELIMITED.value
                @Suppress("UNCHECKED_CAST")
                valueSerializer = ProtoMessageSerializer(valueClass as KClass<ProtoMessage>)
            }
        }
    }

    override fun calculateFullSize(
        tagSize: Int,
        message: ProtoMessage,
        value: Map<K, V>
    ): Int {
        var size = 0
        value.forEach {
            val keySize = keySerializer.unsafeCalculateFullSize(keyTagSize, message, it.key)
            val valueSize = valueSerializer.unsafeCalculateFullSize(valueTagSize, message, it.value)
            val bodySize = keySize + valueSize
            message.cachedSize[it] = bodySize
            size += bodySize + bodySize.varintSize()
        }
        return size + tagSize * value.size
    }

    override fun write(
        tag: Int,
        writer: CodedWriter,
        message: ProtoMessage,
        value: Map<K, V>
    ) {
        value.forEach {
            writer.writeVarint(tag)
            writer.writeVarint(message.cachedSize[it]!!)
            keySerializer.unsafeWrite(keyTag, writer, message, it.key)
            valueSerializer.unsafeWrite(valueTag, writer, message, it.value)
        }
    }
}