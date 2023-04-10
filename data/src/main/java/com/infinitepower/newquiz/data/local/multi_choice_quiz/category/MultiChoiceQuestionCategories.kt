package com.infinitepower.newquiz.data.local.multi_choice_quiz.category

import com.infinitepower.newquiz.model.UiText
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceCategory
import com.infinitepower.newquiz.core.R as CoreR

val multiChoiceQuestionCategories = listOf(
    MultiChoiceCategory(
        key = MultiChoiceBaseCategory.Logo.key,
        id = 1001,
        name = UiText.StringResource(CoreR.string.logo_quiz),
        image = "https://firebasestorage.googleapis.com/v0/b/newquiz-app.appspot.com/o/Illustrations%2Flogo_quiz_illustration.jpg?alt=media&token=cd9e54a2-a5d1-45f1-a285-cc490cc44cad"
    ),
    MultiChoiceCategory(
        key = MultiChoiceBaseCategory.Flag.key,
        id = 1002,
        name = UiText.StringResource(CoreR.string.flag_quiz),
        image = "https://firebasestorage.googleapis.com/v0/b/newquiz-app.appspot.com/o/Illustrations%2Fflags_illustration.png?alt=media&token=ec6b2820-1d26-4352-9c54-201bd387ae94"
    ),
    MultiChoiceCategory(
        key = MultiChoiceBaseCategory.CountryCapitalFlags.key,
        id = 1003,
        name = UiText.StringResource(CoreR.string.country_capital_flags),
        image = "https://firebasestorage.googleapis.com/v0/b/newquiz-app.appspot.com/o/Illustrations%2Fflags_illustration.png?alt=media&token=ec6b2820-1d26-4352-9c54-201bd387ae94"
    ),
    MultiChoiceCategory(
        key = MultiChoiceBaseCategory.GuessMathSolution.key,
        id = 1004,
        name = UiText.StringResource(CoreR.string.guess_solution),
        image = "https://firebasestorage.googleapis.com/v0/b/newquiz-app.appspot.com/o/Illustrations%2Fnumber_illustration.jpg?alt=media&token=68faf243-2b0e-4a13-aa9c-223743e263fd",
        requireInternetConnection = false
    ),
    MultiChoiceCategory(
        key = MultiChoiceBaseCategory.NumberTrivia.key,
        id = 1005,
        name = UiText.StringResource(CoreR.string.number_trivia),
        image = "https://firebasestorage.googleapis.com/v0/b/newquiz-app.appspot.com/o/Illustrations%2Fnumber_12_in_beach.jpg?alt=media&token=9b888c81-c51c-49ac-a376-0b3bde45db36"
    ),
    MultiChoiceCategory(
        key = "General knowledge",
        id = 9,
        name = UiText.StringResource(CoreR.string.general_knowledge),
        image = CoreR.drawable.general_knowledge
    ),
    MultiChoiceCategory(
        key = "Books",
        id = 10,
        name = UiText.StringResource(CoreR.string.entertainment_books),
        image = CoreR.drawable.books
    ),
    MultiChoiceCategory(
        key = "Film",
        id = 11,
        name = UiText.StringResource(CoreR.string.entertainment_film),
        image = CoreR.drawable.films
    ),
    MultiChoiceCategory(
        key = "Music",
        id = 12,
        name = UiText.StringResource(CoreR.string.entertainment_music),
        image = CoreR.drawable.music
    ),
    MultiChoiceCategory(
        key = "General Musicals & Theatres",
        id = 13,
        name = UiText.StringResource(CoreR.string.entertainment_musicals_and_theatres),
        image = CoreR.drawable.musicals_and_theatres
    ),
    MultiChoiceCategory(
        key = "Television",
        id = 14,
        name = UiText.StringResource(CoreR.string.entertainment_television),
        image = CoreR.drawable.entertainment_television
    ),
    MultiChoiceCategory(
        key = "Video Games",
        id = 15,
        name = UiText.StringResource(CoreR.string.entertainment_video_games),
        image = CoreR.drawable.entertainment_video_games
    ),
    MultiChoiceCategory(
        key = "Board Games",
        id = 16,
        name = UiText.StringResource(CoreR.string.entertainment_board_games),
        image = CoreR.drawable.entertainment_board_games
    ),
    MultiChoiceCategory(
        key = "Science & Nature",
        id = 17,
        name = UiText.StringResource(CoreR.string.science_and_nature),
        image = CoreR.drawable.science_and_nature
    ),
    MultiChoiceCategory(
        key = "Computers",
        id = 18,
        name = UiText.StringResource(CoreR.string.science_computers),
        image = CoreR.drawable.science_computers
    ),
    MultiChoiceCategory(
        key = "Mathematics",
        id = 19,
        name = UiText.StringResource(CoreR.string.science_mathematics),
        image = CoreR.drawable.science_mathematics
    ),
    MultiChoiceCategory(
        key = "Mythology",
        id = 20,
        name = UiText.StringResource(CoreR.string.mythology),
        image = CoreR.drawable.mythology
    ),
    MultiChoiceCategory(
        key = "Sports",
        id = 21,
        name = UiText.StringResource(CoreR.string.sports),
        image = CoreR.drawable.sports
    ),
    MultiChoiceCategory(
        key = "Geography",
        id = 22,
        name = UiText.StringResource(CoreR.string.geography),
        image = CoreR.drawable.geography
    ),
    MultiChoiceCategory(
        key = "History",
        id = 23,
        name = UiText.StringResource(CoreR.string.history),
        image = CoreR.drawable.history
    ),
    MultiChoiceCategory(
        key = "Politics",
        id = 24,
        name = UiText.StringResource(CoreR.string.politics),
        image = CoreR.drawable.politics
    ),
    MultiChoiceCategory(
        key = "Art",
        id = 25,
        name = UiText.StringResource(CoreR.string.art),
        image = CoreR.drawable.art
    ),
    MultiChoiceCategory(
        key = "Celebrities",
        id = 26,
        name = UiText.StringResource(CoreR.string.celebrities),
        image = CoreR.drawable.celebrities
    ),
    MultiChoiceCategory(
        key = "Animals",
        id = 27,
        name = UiText.StringResource(CoreR.string.animals),
        image = CoreR.drawable.animals
    ),
    MultiChoiceCategory(
        key = "Vehicles",
        id = 28,
        name = UiText.StringResource(CoreR.string.vehicles),
        image = CoreR.drawable.vehicles
    ),
    MultiChoiceCategory(
        key = "Comics",
        id = 29,
        name = UiText.StringResource(CoreR.string.entertainment_comics),
        image = CoreR.drawable.entertainment_comics
    ),
    MultiChoiceCategory(
        key = "Gadgets",
        id = 30,
        name = UiText.StringResource(CoreR.string.science_gadgets),
        image = CoreR.drawable.science_gadgets
    ),
    MultiChoiceCategory(
        key = "Japanese Anime & Manga",
        id = 31,
        name = UiText.StringResource(CoreR.string.entertainment_japanese_anime_and_manga),
        image = CoreR.drawable.entertainment_japanese_anime_and_manga
    ),
    MultiChoiceCategory(
        key = "Cartoon & Animations",
        id = 32,
        name = UiText.StringResource(CoreR.string.entertainment_cartoon_and_animations),
        image = CoreR.drawable.entertainment_cartoon_and_animations
    )
)