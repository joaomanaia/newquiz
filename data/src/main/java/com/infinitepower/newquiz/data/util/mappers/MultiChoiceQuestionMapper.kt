package com.infinitepower.newquiz.data.util.mappers

import com.infinitepower.newquiz.data.local.multi_choice_quiz.MultiChoiceQuestionEntity
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionType
import com.infinitepower.newquiz.model.multi_choice_quiz.QuestionLanguage
import com.infinitepower.newquiz.model.question.QuestionDifficulty

fun MultiChoiceQuestion.toEntity() = MultiChoiceQuestionEntity(
    id = id,
    description = description,
    imageUrl = imageUrl,
    answers = answers,
    lang = lang.name,
    category = category.toString(),
    correctAns = correctAns,
    type = type.name,
    difficulty = difficulty.toString()
)

fun MultiChoiceQuestionEntity.toQuestion(): MultiChoiceQuestion = MultiChoiceQuestion(
    id = id,
    description = description,
    imageUrl = imageUrl,
    answers = answers,
    lang = QuestionLanguage.EN,
    category = MultiChoiceBaseCategory.fromId(category),
    correctAns = correctAns,
    type = MultiChoiceQuestionType.MULTIPLE,
    difficulty = QuestionDifficulty.from(difficulty)
)
