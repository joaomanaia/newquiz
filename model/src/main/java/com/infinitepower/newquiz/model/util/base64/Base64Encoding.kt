@file:Suppress("MagicNumber")

package com.infinitepower.newquiz.model.util.base64

internal sealed interface Base64Encoding {
    val alphabet: String
    val requiresPadding: Boolean

    object Standard : Base64Encoding {
        override val alphabet: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
        override val requiresPadding: Boolean = true
    }

    object UrlSafe : Base64Encoding {
        override val alphabet: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_"
        override val requiresPadding: Boolean = false // Padding is optional
    }
}

internal fun String.encodeInternal(encoding: Base64Encoding): String {
    val padLength = when (length % 3) {
        1 -> 2
        2 -> 1
        else -> 0
    }

    val raw = this + 0.toChar().toString().repeat(maxOf(0, padLength))

    val encoded = raw.chunkedSequence(3) {
        Triple(it[0].code, it[1].code, it[2].code)
    }.map { (first, second, third) ->
        (0xFF.and(first) shl 16) + (0xFF.and(second) shl 8) + 0xFF.and(third)
    }.map { n ->
        sequenceOf((n shr 18) and 0x3F, (n shr 12) and 0x3F, (n shr 6) and 0x3F, n and 0x3F)
    }.flatten()
        .map { encoding.alphabet[it] }
        .joinToString("")
        .dropLast(padLength)

    return when (encoding.requiresPadding) {
        true -> encoded.padEnd(encoded.length + padLength, '=')
        else -> encoded
    }
}

internal fun String.decodeInternal(encoding: Base64Encoding): Sequence<Int> {
    val padLength = when (length % 4) {
        1 -> 3
        2 -> 2
        3 -> 1
        else -> 0
    }

    return padEnd(length + padLength, '=')
        .replace("=", "A")
        .chunkedSequence(4) {
            (encoding.alphabet.indexOf(it[0]) shl 18) + (encoding.alphabet.indexOf(it[1]) shl 12) +
                    (encoding.alphabet.indexOf(it[2]) shl 6) + encoding.alphabet.indexOf(it[3])
        }
        .map { sequenceOf(0xFF.and(it shr 16), 0xFF.and(it shr 8), 0xFF.and(it)) }
        .flatten()
}

internal fun ByteArray.asCharArray(): CharArray {
    val chars = CharArray(size)
    for (i in chars.indices) {
        chars[i] = get(i).toInt().toChar()
    }
    return chars
}
