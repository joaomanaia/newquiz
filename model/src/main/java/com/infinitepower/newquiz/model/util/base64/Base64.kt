package com.infinitepower.newquiz.model.util.base64

/**
 * Encode a [String] to Base64 standard encoded [String].
 */
val String.base64Encoded: String
    get() = encodeInternal(Base64Encoding.Standard)

/**
 * Encode a [ByteArray] to Base64 standard encoded [String].
 */
val ByteArray.base64Encoded: String
    get() = asCharArray().concatToString().base64Encoded

/**
 * Decode a Base64 standard encoded [String] to [String].
 */
val String.base64Decoded: String
    get() = decodeInternal(Base64Encoding.Standard)
        .map { it.toChar() }
        .joinToString("")
        .dropLast(count { it == '=' })

/**
 * Decode a Base64 standard encoded [String] to [ByteArray].
 */
val String.base64DecodedBytes: ByteArray
    get() = decodeInternal(Base64Encoding.Standard)
        .map { it.toByte() }
        .toList()
        .dropLast(count { it == '=' })
        .toByteArray()

/**
 * Decode a Base64 standard encoded [ByteArray] to [String].
 */
val ByteArray.base64Decoded: String
    get() = asCharArray().concatToString().base64Decoded
