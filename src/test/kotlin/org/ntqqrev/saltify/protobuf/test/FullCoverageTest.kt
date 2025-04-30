package org.ntqqrev.saltify.protobuf.test

import org.ntqqrev.saltify.protobuf.ProtoBuf
import org.ntqqrev.saltify.protobuf.ProtoMessage
import org.ntqqrev.saltify.protobuf.annotation.DisablePacking
import org.ntqqrev.saltify.protobuf.annotation.ProtoField
import org.ntqqrev.saltify.protobuf.annotation.ProtoIgnore
import org.ntqqrev.saltify.protobuf.annotation.ProtoNumberFlag
import org.ntqqrev.saltify.protobuf.annotation.ProtoNumberType
import java.time.Instant
import kotlin.reflect.full.declaredMemberProperties

class FullCoverageTestMessage(
    @ProtoField(1)
    var intField: Int,

    @ProtoNumberType(ProtoNumberFlag.FIXED)
    @ProtoField(2)
    var intFixedField: Int,

    @ProtoNumberType(ProtoNumberFlag.SIGNED)
    @ProtoField(3)
    var intSignedField: Int,

    @ProtoNumberType(ProtoNumberFlag.SIGNED or ProtoNumberFlag.FIXED)
    @ProtoField(4)
    var intSignedFixedField: Int,

    @ProtoField(5)
    var longField: Long,

    @ProtoNumberType(ProtoNumberFlag.FIXED)
    @ProtoField(6)
    var longFixedField: Long,

    @ProtoNumberType(ProtoNumberFlag.SIGNED)
    @ProtoField(7)
    var longSignedField: Long,

    @ProtoNumberType(ProtoNumberFlag.SIGNED or ProtoNumberFlag.FIXED)
    @ProtoField(8)
    var longSignedFixedField: Long,

    @ProtoField(9)
    var intRepeatedField: List<Int>,

    @DisablePacking
    @ProtoField(10)
    var intRepeatedFieldNotPacked: List<Int>,

    @ProtoNumberType(ProtoNumberFlag.FIXED)
    @ProtoField(11)
    var intRepeatedFieldFixed: List<Int>,

    @DisablePacking
    @ProtoNumberType(ProtoNumberFlag.FIXED)
    @ProtoField(12)
    var intRepeatedFieldFixedNotPacked: List<Int>,

    @ProtoNumberType(ProtoNumberFlag.SIGNED)
    @ProtoField(13)
    var intRepeatedFieldSigned: List<Int>,

    @DisablePacking
    @ProtoNumberType(ProtoNumberFlag.SIGNED)
    @ProtoField(14)
    var intRepeatedFieldSignedNotPacked: List<Int>,

    @ProtoNumberType(ProtoNumberFlag.SIGNED or ProtoNumberFlag.FIXED)
    @ProtoField(15)
    var intRepeatedFieldSignedFixed: List<Int>,

    @DisablePacking
    @ProtoNumberType(ProtoNumberFlag.SIGNED or ProtoNumberFlag.FIXED)
    @ProtoField(16)
    var intRepeatedFieldSignedFixedNotPacked: List<Int>,

    @ProtoField(17)
    var longRepeatedField: List<Long>,

    @DisablePacking
    @ProtoField(18)
    var longRepeatedFieldNotPacked: List<Long>,

    @ProtoNumberType(ProtoNumberFlag.FIXED)
    @ProtoField(19)
    var longRepeatedFieldFixed: List<Long>,

    @DisablePacking
    @ProtoNumberType(ProtoNumberFlag.FIXED)
    @ProtoField(20)
    var longRepeatedFieldFixedNotPacked: List<Long>,

    @ProtoNumberType(ProtoNumberFlag.SIGNED)
    @ProtoField(21)
    var longRepeatedFieldSigned: List<Long>,

    @DisablePacking
    @ProtoNumberType(ProtoNumberFlag.SIGNED)
    @ProtoField(22)
    var longRepeatedFieldSignedNotPacked: List<Long>,

    @ProtoNumberType(ProtoNumberFlag.SIGNED or ProtoNumberFlag.FIXED)
    @ProtoField(23)
    var longRepeatedFieldSignedFixed: List<Long>,

    @DisablePacking
    @ProtoNumberType(ProtoNumberFlag.SIGNED or ProtoNumberFlag.FIXED)
    @ProtoField(24)
    var longRepeatedFieldSignedFixedNotPacked: List<Long>,

    @ProtoField(25)
    var floatField: Float,

    @ProtoField(26)
    var floatRepeatedField: List<Float>,

    @DisablePacking
    @ProtoField(27)
    var floatRepeatedFieldNotPacked: List<Float>,

    @ProtoField(28)
    var doubleField: Double,

    @ProtoField(29)
    var doubleRepeatedField: List<Double>,

    @DisablePacking
    @ProtoField(30)
    var doubleRepeatedFieldNotPacked: List<Double>,

    @ProtoField(31)
    var booleanField: Boolean,

    @ProtoField(32)
    var booleanRepeatedField: List<Boolean>,

    @DisablePacking
    @ProtoField(33)
    var booleanRepeatedFieldNotPacked: List<Boolean>,

    @ProtoField(34)
    var stringField: String,

    @ProtoField(35)
    var stringRepeatedField: List<String>,

    @ProtoField(36)
    var byteArrayField: ByteArray,

    @ProtoField(37)
    var byteArrayRepeatedField: List<ByteArray>,

    @ProtoField(38)
    var nestedField: Nested,

    @ProtoField(39)
    var nestedRepeatedField: List<Nested>,

    @ProtoField(40)
    var veryBigIntField: Int = 0x7FFFFFFF - 1,

    @ProtoField(41)
    var veryBigLongField: Long = 0x7FFFFFFFFFFFFFFF - 1,

    @ProtoField(42)
    var optionalField: String? = null,

    @ProtoField(43)
    var mapField: Map<String, Int> = mapOf(
        "key1" to 1,
        "key2" to 2,
        "key3" to 3,
    ),

    @ProtoIgnore
    var ignoredField: java.time.Instant = java.time.Instant.now(),
) : ProtoMessage() {
    class Nested(
        @ProtoField(1)
        var nested: Int
    ): ProtoMessage()
}

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    val message = FullCoverageTestMessage(
        1,
        2,
        3,
        4,
        1L,
        2L,
        3L,
        4L,
        listOf(1, 2, 3),
        listOf(4, 5, 6),
        listOf(7, 8, 9),
        listOf(10, 11, 12),
        listOf(13, 14, 15),
        listOf(16, 17, 18),
        listOf(19, 20, 21),
        listOf(22, 23, 24),
        listOf(1L, 2L, 3L),
        listOf(4L, 5L, 6L),
        listOf(7L, 8L, 9L),
        listOf(10L, 11L, 12L),
        listOf(13L, 14L, 15L),
        listOf(16L, 17L, 18L),
        listOf(19L, 20L, 21L),
        listOf(22L, 23L, 24L),
        0.1f,
        listOf(0.2f, 0.3f, 0.4f),
        listOf(0.5f, 0.6f, 0.7f),
        0.1,
        listOf(0.2, 0.3, 0.4),
        listOf(0.5, 0.6, 0.7),
        true,
        listOf(false, true, false),
        listOf(true, false, true),
        "Hello, World!",
        listOf("Hello", "World"),
        byteArrayOf(1, 2, 3),
        listOf(byteArrayOf(4, 5, 6), byteArrayOf(7, 8, 9)),
        FullCoverageTestMessage.Nested(
            nested = 1
        ),
        listOf(
            FullCoverageTestMessage.Nested(
                nested = 2
            ),
            FullCoverageTestMessage.Nested(
                nested = 3
            )
        ),
    )

    val serializeStart = System.currentTimeMillis()
    val serialized = ProtoBuf.serialize(message)
    val serializeEnd = System.currentTimeMillis()
    println("Serialized in ${serializeEnd - serializeStart}ms")

    val repeatedStart = System.currentTimeMillis()
    repeat(100000) {
        ProtoBuf.serialize(message)
    }
    val repeatedEnd = System.currentTimeMillis()
    println("Repeated serialization in ${repeatedEnd - repeatedStart}ms")

    System.gc()
    Thread.sleep(1000)

    val deserialized = ProtoBuf.deserialize<FullCoverageTestMessage>(serialized)

    val deserializeStart = System.currentTimeMillis()
    repeat(100000) {
        ProtoBuf.deserialize<FullCoverageTestMessage>(serialized)
    }
    val deserializeEnd = System.currentTimeMillis()
    println("Repeated deserialization in ${deserializeEnd - deserializeStart}ms")

    println("Serialized:" + serialized.toHexString())
    println("Deserialized:" + reflectionToString(deserialized))
}

// Use reflection to print the string representation of a class
// Include the class name, package name, and all fields with their values
fun reflectionToString(obj: Any, indent: Int = 1): String {
    val className = obj::class.simpleName
    val packageName = obj::class.qualifiedName?.substringBeforeLast('.')
    val fields = obj::class.declaredMemberProperties
        .joinToString(",\n") { f ->
            "${
                "    ".repeat(indent)
            }${f.name}=${
                when (val value = f.getter.call(obj)) {
                    is ByteArray -> value.joinToString("") { it.toString(16) }
                    is String -> "\"$value\""
                    is Int, is Long, is Float, is Double, is Boolean, is Instant -> value.toString()
                    is List<*> -> value.joinToString(", ", "[", "]") {
                        when (it) {
                            is ByteArray -> it.joinToString("") { byte -> byte.toString(16) }
                            is String -> "\"$it\""
                            is Int, is Long, is Float, is Double, is Boolean, is Instant -> it.toString()
                            null -> "null"
                            else -> reflectionToString(it, indent + 1)
                        }
                    }
                    is Map<*, *> -> value.entries.joinToString(", ", "{", "}") {
                        "${it.key}=${
                            when (val mapValue = it.value) {
                                is ByteArray -> mapValue.joinToString("") { byte -> byte.toString(16) }
                                is String -> "\"$mapValue\""
                                is Int, is Long, is Float, is Double, is Boolean, is Instant -> mapValue.toString()
                                null -> "null"
                                else -> reflectionToString(mapValue, indent + 1)
                            }
                        }"
                    }
                    null -> "null"
                    else -> reflectionToString(value, indent + 1)
                }
            }" }
    return "$packageName.$className(\n$fields\n${"    ".repeat(indent - 1)})"
}
