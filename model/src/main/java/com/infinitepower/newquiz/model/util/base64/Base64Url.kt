package com.infinitepower.newquiz.model.util.base64

/**
 * Encode a [String] to Base64 URL-safe encoded [String].
 */
val String.base64UrlEncoded: String
    get() = encodeInternal(Base64Encoding.UrlSafe)

/**
 * Encode a [ByteArray] to Base64 URL-safe encoded [String].
 */
val ByteArray.base64UrlEncoded: String
    get() = asCharArray().concatToString().base64UrlEncoded

/**
 * Decode a Base64 URL-safe encoded [String] to [String].
 */
val String.base64UrlDecoded: String
    get() {
        val ret = decodeInternal(Base64Encoding.UrlSafe).map { it.toChar() }
        val foo = ret.joinToString("")
        val bar = foo.dropLast(count { it == '=' })
        return bar.filterNot { it.code == 0 }
    }

/**
 * Decode a Base64 URL-safe encoded [String] to [ByteArray].
 */
val String.base64UrlDecodedBytes: ByteArray
    get() = decodeInternal(Base64Encoding.UrlSafe)
        .map { it.toByte() }
        .toList()
        .dropLast(count { it == '=' })
        .toByteArray()

/**
 * Decode a Base64 URL-safe encoded [ByteArray] to [String].
 */
val ByteArray.base64UrlDecoded: String
    get() = asCharArray().concatToString().base64UrlDecoded
