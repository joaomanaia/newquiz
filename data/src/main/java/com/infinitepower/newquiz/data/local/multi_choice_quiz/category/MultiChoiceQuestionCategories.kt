package com.infinitepower.newquiz.data.local.multi_choice_quiz.category

import com.infinitepower.newquiz.model.UiText
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceCategory
import com.infinitepower.newquiz.core.R as CoreR

val multiChoiceQuestionCategories = listOf(
    MultiChoiceCategory(
        id = MultiChoiceBaseCategory.Logo.id,
        name = UiText.StringResource(CoreR.string.logo_quiz),
        image = "https://firebasestorage.googleapis.com/v0/b/newquiz-app.appspot.com/o/Illustrations%2Flogo_quiz_illustration.jpg?alt=media&token=cd9e54a2-a5d1-45f1-a285-cc490cc44cad"
    ),
    MultiChoiceCategory(
        id = MultiChoiceBaseCategory.Flag.id,
        name = UiText.StringResource(CoreR.string.flag_quiz),
        image = "https://firebasestorage.googleapis.com/v0/b/newquiz-app.appspot.com/o/Illustrations%2Fflags_illustration.png?alt=media&token=ec6b2820-1d26-4352-9c54-201bd387ae94"
    ),
    MultiChoiceCategory(
        id = MultiChoiceBaseCategory.CountryCapitalFlags.id,
        name = UiText.StringResource(CoreR.string.country_capital_flags),
        image = "https://firebasestorage.googleapis.com/v0/b/newquiz-app.appspot.com/o/Illustrations%2Fflags_illustration.png?alt=media&token=ec6b2820-1d26-4352-9c54-201bd387ae94"
    ),
    MultiChoiceCategory(
        id = MultiChoiceBaseCategory.GuessMathSolution.id,
        name = UiText.StringResource(CoreR.string.guess_solution),
        image = "https://firebasestorage.googleapis.com/v0/b/newquiz-app.appspot.com/o/Illustrations%2Fnumber_illustration.jpg?alt=media&token=68faf243-2b0e-4a13-aa9c-223743e263fd",
        requireInternetConnection = false
    ),
    MultiChoiceCategory(
        id = MultiChoiceBaseCategory.NumberTrivia.id,
        name = UiText.StringResource(CoreR.string.number_trivia),
        image = "https://firebasestorage.googleapis.com/v0/b/newquiz-app.appspot.com/o/Illustrations%2Fnumber_12_in_beach.jpg?alt=media&token=9b888c81-c51c-49ac-a376-0b3bde45db36"
    ),
    MultiChoiceCategory(
        id = "9",
        name = UiText.StringResource(CoreR.string.general_knowledge),
        image = CoreR.drawable.general_knowledge
    ),
    MultiChoiceCategory(
        id = "10",
        name = UiText.StringResource(CoreR.string.entertainment_books),
        image = CoreR.drawable.books
    ),
    MultiChoiceCategory(
        id = "11",
        name = UiText.StringResource(CoreR.string.entertainment_film),
        image = CoreR.drawable.films
    ),
    MultiChoiceCategory(
        id = "12",
        name = UiText.StringResource(CoreR.string.entertainment_music),
        image = CoreR.drawable.music
    ),
    MultiChoiceCategory(
        id = "13",
        name = UiText.StringResource(CoreR.string.entertainment_musicals_and_theatres),
        image = CoreR.drawable.musicals_and_theatres
    ),
    MultiChoiceCategory(
        id = "14",
        name = UiText.StringResource(CoreR.string.entertainment_television),
        image = CoreR.drawable.entertainment_television
    ),
    MultiChoiceCategory(
        id = "15",
        name = UiText.StringResource(CoreR.string.entertainment_video_games),
        image = CoreR.drawable.entertainment_video_games
    ),
    MultiChoiceCategory(
        id = "16",
        name = UiText.StringResource(CoreR.string.entertainment_board_games),
        image = CoreR.drawable.entertainment_board_games
    ),
    MultiChoiceCategory(
        id = "17",
        name = UiText.StringResource(CoreR.string.science_and_nature),
        image = CoreR.drawable.science_and_nature
    ),
    MultiChoiceCategory(
        id = "18",
        name = UiText.StringResource(CoreR.string.science_computers),
        image = CoreR.drawable.science_computers
    ),
    MultiChoiceCategory(
        id = "19",
        name = UiText.StringResource(CoreR.string.science_mathematics),
        image = CoreR.drawable.science_mathematics
    ),
    MultiChoiceCategory(
        id = "20",
        name = UiText.StringResource(CoreR.string.mythology),
        image = CoreR.drawable.mythology
    ),
    MultiChoiceCategory(
        id = "21",
        name = UiText.StringResource(CoreR.string.sports),
        image = CoreR.drawable.sports
    ),
    MultiChoiceCategory(
        id = "22",
        name = UiText.StringResource(CoreR.string.geography),
        image = CoreR.drawable.geography
    ),
    MultiChoiceCategory(
        id = "23",
        name = UiText.StringResource(CoreR.string.history),
        image = CoreR.drawable.history
    ),
    MultiChoiceCategory(
        id = "24",
        name = UiText.StringResource(CoreR.string.politics),
        image = CoreR.drawable.politics
    ),
    MultiChoiceCategory(
        id = "25",
        name = UiText.StringResource(CoreR.string.art),
        image = CoreR.drawable.art
    ),
    MultiChoiceCategory(
        id = "26",
        name = UiText.StringResource(CoreR.string.celebrities),
        image = CoreR.drawable.celebrities
    ),
    MultiChoiceCategory(
        id = "27",
        name = UiText.StringResource(CoreR.string.animals),
        image = CoreR.drawable.animals
    ),
    MultiChoiceCategory(
        id = "28",
        name = UiText.StringResource(CoreR.string.vehicles),
        image = CoreR.drawable.vehicles
    ),
    MultiChoiceCategory(
        id = "29",
        name = UiText.StringResource(CoreR.string.entertainment_comics),
        image = CoreR.drawable.entertainment_comics
    ),
    MultiChoiceCategory(
        id = "30",
        name = UiText.StringResource(CoreR.string.science_gadgets),
        image = CoreR.drawable.science_gadgets
    ),
    MultiChoiceCategory(
        id = "31",
        name = UiText.StringResource(CoreR.string.entertainment_japanese_anime_and_manga),
        image = CoreR.drawable.entertainment_japanese_anime_and_manga
    ),
    MultiChoiceCategory(
        id = "32",
        name = UiText.StringResource(CoreR.string.entertainment_cartoon_and_animations),
        image = CoreR.drawable.entertainment_cartoon_and_animations
    )
)