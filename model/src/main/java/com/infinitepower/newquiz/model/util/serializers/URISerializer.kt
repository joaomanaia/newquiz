package com.infinitepower.newquiz.model.util.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.net.URI

object URISerializer : KSerializer<URI> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = "URI",
        kind = PrimitiveKind.STRING
    )

    override fun serialize(encoder: Encoder, value: URI) {
        encoder.encodeString(value.toASCIIString())
    }

    override fun deserialize(decoder: Decoder): URI {
        return URI.create(decoder.decodeString())
    }
}
