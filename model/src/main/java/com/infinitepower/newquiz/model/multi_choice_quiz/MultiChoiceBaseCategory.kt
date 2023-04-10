package com.infinitepower.newquiz.model.multi_choice_quiz

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = MultiChoiceBaseCategorySerializer::class)
sealed class MultiChoiceBaseCategory(
    val key: String,
) : java.io.Serializable {
    companion object {
        fun fromKey(key: String) = when (key) {
            Logo.key -> Logo
            Flag.key -> Flag
            GuessMathSolution.key -> GuessMathSolution
            NumberTrivia.key -> NumberTrivia
            CountryCapitalFlags.key -> CountryCapitalFlags
            else -> Normal(key)
        }
    }

    override fun toString(): String = key

    val hasCategory: Boolean
        get() = key.isNotBlank() && key != "random"

    /**
     * Random multi choice category using [Normal] class
     */
    object Random : Normal()

    /**
     * Normal multi choice type with category
     * @param categoryKey category to the quiz
     */
    open class Normal(
        val categoryKey: String
    ) : MultiChoiceBaseCategory(key = categoryKey) {
        /** Sets multi choice type as no category */
        constructor() : this("random")

        override fun toString(): String = categoryKey

        override fun equals(other: Any?): Boolean {
            if (other !is Normal) return false

            return this.categoryKey == other.categoryKey
        }

        override fun hashCode(): Int = categoryKey.hashCode()
    }

    /** Logo multi choice quiz category */
    object Logo : MultiChoiceBaseCategory(key = "logo")

    /** Flag multi choice quiz category */
    object Flag : MultiChoiceBaseCategory(key = "flag")

    /** Number trivia multi choice quiz category */
    object CountryCapitalFlags : MultiChoiceBaseCategory(key = "country_capital_flags")

    /** Guess math solution multi choice quiz category */
    object GuessMathSolution : MultiChoiceBaseCategory(key = "guess_math_solution")

    /** Number trivia multi choice quiz category */
    object NumberTrivia : MultiChoiceBaseCategory(key = "number_trivia")
}

object MultiChoiceBaseCategorySerializer : KSerializer<MultiChoiceBaseCategory> {
    override fun serialize(encoder: Encoder, value: MultiChoiceBaseCategory) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): MultiChoiceBaseCategory {
        return MultiChoiceBaseCategory.fromKey(decoder.decodeString())
    }

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Category", PrimitiveKind.STRING)
}
