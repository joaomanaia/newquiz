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
    val id: String,
) : java.io.Serializable {
    companion object {
        fun fromId(id: String) = when (id) {
            Logo.id -> Logo
            Flag.id -> Flag
            GuessMathSolution.id -> GuessMathSolution
            NumberTrivia.id -> NumberTrivia
            CountryCapitalFlags.id -> CountryCapitalFlags
            else -> Normal(id)
        }
    }

    override fun toString(): String = id

    val hasCategory: Boolean
        get() = id.isNotBlank() && id != "random"

    /**
     * Random multi choice category using [Normal] class
     */
    object Random : Normal()

    /**
     * Normal multi choice type with category
     * @param categoryId category to the quiz
     */
    open class Normal(
        val categoryId: String
    ) : MultiChoiceBaseCategory(id = categoryId) {
        /** Sets multi choice type as no category */
        constructor() : this("random")

        override fun toString(): String = categoryId

        override fun equals(other: Any?): Boolean {
            if (other !is Normal) return false

            return this.categoryId == other.categoryId
        }

        override fun hashCode(): Int = categoryId.hashCode()
    }

    /** Logo multi choice quiz category */
    object Logo : MultiChoiceBaseCategory(id = "logo")

    /** Flag multi choice quiz category */
    object Flag : MultiChoiceBaseCategory(id = "flag")

    /** Number trivia multi choice quiz category */
    object CountryCapitalFlags : MultiChoiceBaseCategory(id = "country_capital_flags")

    /** Guess math solution multi choice quiz category */
    object GuessMathSolution : MultiChoiceBaseCategory(id = "guess_math_solution")

    /** Number trivia multi choice quiz category */
    object NumberTrivia : MultiChoiceBaseCategory(id = "number_trivia")
}

object MultiChoiceBaseCategorySerializer : KSerializer<MultiChoiceBaseCategory> {
    override fun serialize(encoder: Encoder, value: MultiChoiceBaseCategory) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): MultiChoiceBaseCategory {
        return MultiChoiceBaseCategory.fromId(decoder.decodeString())
    }

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Category", PrimitiveKind.STRING)
}
