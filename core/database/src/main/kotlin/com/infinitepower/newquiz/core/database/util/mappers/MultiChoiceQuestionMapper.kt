package com.infinitepower.newquiz.core.database.util.mappers

import com.infinitepower.newquiz.core.database.model.MultiChoiceQuestionEntity
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionType
import com.infinitepower.newquiz.model.multi_choice_quiz.QuestionLanguage
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import java.net.URI

fun MultiChoiceQuestion.toEntity(): MultiChoiceQuestionEntity {
    return MultiChoiceQuestionEntity(
        id = id,
        description = description,
        imageUrl = image?.toASCIIString(),
        answers = answers,
        lang = lang.name,
        category = category.toString(),
        correctAns = correctAns,
        type = type.name,
        difficulty = difficulty.toString()
    )
}

fun MultiChoiceQuestionEntity.toModel(): MultiChoiceQuestion = MultiChoiceQuestion(
    id = id,
    description = description,
    image = imageUrl?.let { URI.create(it) },
    answers = answers,
    lang = QuestionLanguage.EN,
    category = MultiChoiceBaseCategory.fromId(category),
    correctAns = correctAns,
    type = MultiChoiceQuestionType.MULTIPLE,
    difficulty = QuestionDifficulty.from(difficulty)
)
