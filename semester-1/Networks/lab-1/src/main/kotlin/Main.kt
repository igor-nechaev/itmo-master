package com.example.exmple

fun main() {
    val n = readln().toInt()
    val messages = readln().split(" ")
    labFirst(n, messages).forEach { receivedMessage ->
        println(receivedMessage)
    }
}

fun labFirst(n: Int, messages: List<String>): List<String> {
    val walshCodes = getWalshCodes(n)
    val baseStations = buildList {
        repeat(n) { index ->
            add(
                BaseStation(
                    message = messages[index],
                    code = walshCodes[index]
                )
            )
        }
    }

    val signal = baseStations.map { baseStation ->
        baseStation.message.toBits().toPlusMinus().applyCode(baseStation.code)
    }.multiplex()

    return baseStations.map { baseStation ->
        signal.decodeBits(baseStation.code).decodeToString()
    }
}

class BaseStation(
    val message: String,
    val code: IntArray,
)

fun String.toBits(): IntArray =
    flatMap { char ->
        char.code.toString(2)
            .padStart(8, '0')
            .map { char -> char.digitToInt() }
    }.toIntArray()

fun IntArray.toPlusMinus(): IntArray = map { value ->
    when (value) {
        0 -> -1
        else -> 1
    }
}.toIntArray()

fun IntArray.applyCode(code: IntArray): IntArray =
    flatMap { bit -> code.map { codeBit -> bit * codeBit } }.toIntArray()

fun List<IntArray>.multiplex(): IntArray =
    reduce { acc, signal -> acc.zip(signal) { a, b -> a + b }.toIntArray() }

fun IntArray.decodeBits(code: IntArray): IntArray {
    return toList()
        .chunked(code.size) { chunk ->
            val correlation = chunk.indices.sumOf { j -> chunk[j] * code[j] }
            when (correlation >= 0) {
                true -> 1
                false -> 0
            }
        }
        .toIntArray()
}

fun IntArray.decodeToString(): String {
    return toList()
        .chunked(8) { chunk ->
            chunk.fold(0) { acc, bit -> (acc shl 1) or bit }.toChar()
        }
        .joinToString("")
}

fun getWalshCodes(n: Int): Array<IntArray> {
    val hadamard = Array(n) { IntArray(n) }
    hadamard[0][0] = 1
    var k = 1
    while (k < n) {
        for (i in 0..< k) {
            for (j in 0..< k) {
                hadamard[i + k][j] = hadamard[i][j]
                hadamard[i][j + k] = hadamard[i][j]
                hadamard[i + k][j + k] = -hadamard[i][j]
            }
        }
        k += k
    }

    return hadamard
}
