package org.ntqqrev.saltify.protobuf.model

import org.ntqqrev.saltify.protobuf.ProtoMessage
import org.ntqqrev.saltify.protobuf.annotation.DisablePacking
import org.ntqqrev.saltify.protobuf.annotation.ProtoField
import org.ntqqrev.saltify.protobuf.annotation.ProtoNumberFlag
import org.ntqqrev.saltify.protobuf.annotation.ProtoNumberType
import org.ntqqrev.saltify.protobuf.deserializer.*
import org.ntqqrev.saltify.protobuf.serializer.*
import org.ntqqrev.saltify.protobuf.util.varintSize
import java.lang.invoke.MethodHandles
import java.lang.reflect.Field
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.javaGetter
import kotlin.reflect.jvm.javaSetter

private val lookup = MethodHandles.lookup()

internal class ProtoFieldDescriptor(kProperty: KMutableProperty<*>) : Comparable<ProtoFieldDescriptor> {
    val fieldName: String = kProperty.name

    val field: Field = kProperty.javaField
        ?: throw IllegalArgumentException("Field $fieldName is not a valid Java field")

    val underlyingType: Class<*> = field.type

    val annotations = kProperty.javaField?.annotations

    val fieldNumber: Int = annotations
        ?.filterIsInstance<ProtoField>()
        ?.firstOrNull()?.value
        ?: throw IllegalArgumentException("Field $fieldName is not annotated with ProtoField")

    val isOptional: Boolean = kProperty.returnType.isMarkedNullable

    val writeTag: Int
    val isRepeated: Boolean
    val isPacked: Boolean
    val tagSize: Int
    val getter = lookup.unreflect(kProperty.javaGetter)!!
    val setter = lookup.unreflect(kProperty.javaSetter)!!
    val serializer: ProtoFieldSerializer<*>
    val deserializer: ProtoFieldDeserializer

    init {
        field.trySetAccessible()

        when (underlyingType) {
            Int::class.java -> {
                when (annotations?.filterIsInstance<ProtoNumberType>()?.firstOrNull()?.flag) {
                    ProtoNumberFlag.FIXED -> {
                        writeTag = fieldNumber shl 3 or WireType.FIXED32.value
                        serializer = IntFixed32Serializer
                        deserializer = IntFixed32Deserializer
                    }

                    ProtoNumberFlag.SIGNED -> {
                        writeTag = fieldNumber shl 3 or WireType.VARINT.value
                        serializer = IntZigzagVarintSerializer
                        deserializer = IntZigzagVarintDeserializer
                    }

                    ProtoNumberFlag.FIXED or ProtoNumberFlag.SIGNED -> {
                        writeTag = fieldNumber shl 3 or WireType.FIXED32.value
                        serializer = IntZigzagFixed32Serializer
                        deserializer = IntZigzagFixed32Deserializer
                    }

                    else -> {
                        writeTag = fieldNumber shl 3 or WireType.VARINT.value
                        serializer = IntVarintSerializer
                        deserializer = IntVarintDeserializer
                    }
                }
                isRepeated = false
                isPacked = false
            }

            Long::class.java -> {
                when (annotations?.filterIsInstance<ProtoNumberType>()?.firstOrNull()?.flag) {
                    ProtoNumberFlag.FIXED -> {
                        writeTag = fieldNumber shl 3 or WireType.FIXED64.value
                        serializer = LongFixed64Serializer
                        deserializer = LongFixed64Deserializer
                    }

                    ProtoNumberFlag.SIGNED -> {
                        writeTag = fieldNumber shl 3 or WireType.VARINT.value
                        serializer = LongZigzagVarintSerializer
                        deserializer = LongZigzagVarintDeserializer
                    }

                    ProtoNumberFlag.FIXED or ProtoNumberFlag.SIGNED -> {
                        writeTag = fieldNumber shl 3 or WireType.FIXED64.value
                        serializer = LongZigzagFixed64Serializer
                        deserializer = LongZigzagFixed64Deserializer
                    }

                    else -> {
                        writeTag = fieldNumber shl 3 or WireType.VARINT.value
                        serializer = LongVarintSerializer
                        deserializer = LongVarintDeserializer
                    }
                }
                isRepeated = false
                isPacked = false
            }

            Float::class.java -> {
                writeTag = fieldNumber shl 3 or WireType.FIXED32.value
                serializer = FloatSerializer
                deserializer = FloatDeserializer
                isRepeated = false
                isPacked = false
            }

            Double::class.java -> {
                writeTag = fieldNumber shl 3 or WireType.FIXED64.value
                serializer = DoubleSerializer
                deserializer = DoubleDeserializer
                isRepeated = false
                isPacked = false
            }

            Boolean::class.java -> {
                writeTag = fieldNumber shl 3 or WireType.VARINT.value
                serializer = BooleanSerializer
                deserializer = BooleanDeserializer
                isRepeated = false
                isPacked = false
            }

            ByteArray::class.java -> {
                writeTag = fieldNumber shl 3 or WireType.LENGTH_DELIMITED.value
                serializer = ByteArraySerializer
                deserializer = ByteArrayDeserializer
                isRepeated = false
                isPacked = false
            }

            String::class.java -> {
                writeTag = fieldNumber shl 3 or WireType.LENGTH_DELIMITED.value
                serializer = StringSerializer
                deserializer = StringDeserializer
                isRepeated = false
                isPacked = false
            }

            List::class.java -> {
                val genericType = kProperty.returnType.arguments.firstOrNull()?.type?.classifier
                if (genericType == null) {
                    throw IllegalArgumentException("Field $fieldName is a List but has no generic type")
                }
                isRepeated = true
                isPacked = annotations?.filterIsInstance<DisablePacking>()?.isEmpty() == true

                val flag = annotations?.filterIsInstance<ProtoNumberType>()?.firstOrNull()?.flag
                    ?: ProtoNumberFlag.VARINT

                when (genericType) {
                    Int::class -> {
                        if (isPacked) {
                            writeTag = fieldNumber shl 3 or WireType.LENGTH_DELIMITED.value
                            serializer = when (flag) {
                                ProtoNumberFlag.FIXED -> IntPackedFixed32Serializer
                                ProtoNumberFlag.SIGNED -> IntZigzagPackedVarintSerializer
                                ProtoNumberFlag.FIXED or ProtoNumberFlag.SIGNED -> IntZigzagPackedFixed32Serializer
                                else -> IntPackedVarintSerializer
                            }
                        } else {
                            writeTag = fieldNumber shl 3 or
                                    if (flag and ProtoNumberFlag.FIXED == 1) WireType.FIXED32.value
                                    else WireType.VARINT.value

                            serializer = when (flag) {
                                ProtoNumberFlag.FIXED -> IntNotPackedFixed32Serializer
                                ProtoNumberFlag.SIGNED -> IntZigzagNotPackedVarintSerializer
                                ProtoNumberFlag.FIXED or ProtoNumberFlag.SIGNED -> IntZigzagNotPackedFixed32Serializer
                                else -> IntNotPackedVarintSerializer
                            }
                        }
                        deserializer = when (flag) {
                            ProtoNumberFlag.FIXED -> IntRepeatedFixed32Deserializer
                            ProtoNumberFlag.SIGNED -> IntRepeatedZigzagVarintDeserializer
                            ProtoNumberFlag.FIXED or ProtoNumberFlag.SIGNED -> IntRepeatedZigzagFixed32Deserializer
                            else -> IntRepeatedVarintDeserializer
                        }
                    }

                    Long::class -> {
                        if (isPacked) {
                            writeTag = fieldNumber shl 3 or WireType.LENGTH_DELIMITED.value
                            serializer = when (flag) {
                                ProtoNumberFlag.FIXED -> LongPackedFixed64Serializer
                                ProtoNumberFlag.SIGNED -> LongZigzagPackedVarintSerializer
                                ProtoNumberFlag.FIXED or ProtoNumberFlag.SIGNED -> LongZigzagPackedFixed64Serializer
                                else -> LongPackedVarintSerializer
                            }
                        } else {
                            writeTag = fieldNumber shl 3 or
                                    if (flag and ProtoNumberFlag.FIXED == 1) WireType.FIXED64.value
                                    else WireType.VARINT.value

                            serializer = when (flag) {
                                ProtoNumberFlag.FIXED -> LongNotPackedFixed64Serializer
                                ProtoNumberFlag.SIGNED -> LongZigzagNotPackedVarintSerializer
                                ProtoNumberFlag.FIXED or ProtoNumberFlag.SIGNED -> LongZigzagNotPackedFixed64Serializer
                                else -> LongNotPackedVarintSerializer
                            }
                        }
                        deserializer = when (flag) {
                            ProtoNumberFlag.FIXED -> LongRepeatedFixed64Deserializer
                            ProtoNumberFlag.SIGNED -> LongRepeatedZigzagVarintDeserializer
                            ProtoNumberFlag.FIXED or ProtoNumberFlag.SIGNED -> LongRepeatedZigzagFixed64Deserializer
                            else -> LongRepeatedVarintDeserializer
                        }
                    }

                    Float::class -> {
                        if (isPacked) {
                            writeTag = fieldNumber shl 3 or WireType.LENGTH_DELIMITED.value
                            serializer = FloatPackedSerializer
                        } else {
                            writeTag = fieldNumber shl 3 or WireType.FIXED32.value
                            serializer = FloatNotPackedSerializer
                        }
                        deserializer = FloatRepeatedDeserializer
                    }

                    Double::class -> {
                        if (isPacked) {
                            writeTag = fieldNumber shl 3 or WireType.LENGTH_DELIMITED.value
                            serializer = DoublePackedSerializer
                        } else {
                            writeTag = fieldNumber shl 3 or WireType.FIXED64.value
                            serializer = DoubleNotPackedSerializer
                        }
                        deserializer = DoubleRepeatedDeserializer
                    }

                    Boolean::class -> {
                        if (isPacked) {
                            writeTag = fieldNumber shl 3 or WireType.LENGTH_DELIMITED.value
                            serializer = BooleanPackedSerializer
                        } else {
                            writeTag = fieldNumber shl 3 or WireType.VARINT.value
                            serializer = BooleanNotPackedSerializer
                        }
                        deserializer = BooleanRepeatedDeserializer
                    }

                    String::class -> {
                        writeTag = fieldNumber shl 3 or WireType.LENGTH_DELIMITED.value
                        serializer = StringRepeatedSerializer
                        deserializer = StringRepeatedDeserializer
                    }

                    ByteArray::class -> {
                        writeTag = fieldNumber shl 3 or WireType.LENGTH_DELIMITED.value
                        serializer = ByteArrayRepeatedSerializer
                        deserializer = ByteArrayRepeatedDeserializer
                    }

                    else -> {
                        if (genericType is KClass<*> && genericType.isSubclassOf(ProtoMessage::class)) {
                            writeTag = fieldNumber shl 3 or WireType.LENGTH_DELIMITED.value
                            @Suppress("UNCHECKED_CAST")
                            serializer = ProtoMessageRepeatedSerializer(genericType as KClass<ProtoMessage>)
                            deserializer = ProtoMessageRepeatedDeserializer(genericType)
                        } else {
                            throw IllegalArgumentException("Field $fieldName is a List but has an unsupported generic type: $genericType")
                        }
                    }
                }
            }

            Map::class.java -> {
                val genericTypes = kProperty.returnType.arguments
                if (genericTypes.size != 2) {
                    throw IllegalArgumentException("Field $fieldName is a Map but has no generic types")
                }
                isRepeated = true
                isPacked = false

                val keyClass = genericTypes[0].type?.classifier
                val valueClass = genericTypes[1].type?.classifier

                if (keyClass == null || valueClass == null) {
                    throw IllegalArgumentException("Field $fieldName is a Map but has no generic types")
                }

                writeTag = fieldNumber shl 3 or WireType.LENGTH_DELIMITED.value
                serializer = MapSerializer(keyClass as KClass<*>, valueClass as KClass<*>)
                deserializer = MapDeserializer(keyClass, valueClass)
            }

            else -> {
                val kPropertyReturnType = kProperty.returnType.classifier
                if (kPropertyReturnType is KClass<*> && kPropertyReturnType.isSubclassOf(ProtoMessage::class)) {
                    writeTag = fieldNumber shl 3 or WireType.LENGTH_DELIMITED.value
                    @Suppress("UNCHECKED_CAST")
                    serializer = ProtoMessageSerializer(kPropertyReturnType as KClass<ProtoMessage>)
                    deserializer = ProtoMessageDeserializer(kPropertyReturnType)
                    isRepeated = false
                    isPacked = false
                } else {
                    throw IllegalArgumentException("Field $fieldName is not a supported type: $underlyingType")
                }
            }
        }

        tagSize = writeTag.varintSize()
    }

    override fun compareTo(other: ProtoFieldDescriptor): Int = fieldNumber.compareTo(other.fieldNumber)
}