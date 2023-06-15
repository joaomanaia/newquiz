package com.infinitepower.newquiz.wordle.di

import com.infinitepower.newquiz.data.di.RepositoryModule
import com.infinitepower.newquiz.data.repository.comparison_quiz.ComparisonQuizRepositoryImpl
import com.infinitepower.newquiz.data.repository.daily_challenge.DailyChallengeRepositoryImpl
import com.infinitepower.newquiz.data.repository.home.RecentCategoriesRepositoryImpl
import com.infinitepower.newquiz.data.repository.math_quiz.MathQuizCoreRepositoryImpl
import com.infinitepower.newquiz.data.repository.maze_quiz.MazeQuizRepositoryImpl
import com.infinitepower.newquiz.data.repository.multi_choice_quiz.CountryCapitalFlagsQuizRepositoryImpl
import com.infinitepower.newquiz.data.repository.multi_choice_quiz.FlagQuizRepositoryImpl
import com.infinitepower.newquiz.data.repository.multi_choice_quiz.GuessMathSolutionRepositoryImpl
import com.infinitepower.newquiz.data.repository.multi_choice_quiz.LogoQuizRepositoryImpl
import com.infinitepower.newquiz.data.repository.multi_choice_quiz.MultiChoiceQuestionRepositoryImpl
import com.infinitepower.newquiz.data.repository.multi_choice_quiz.saved_questions.SavedMultiChoiceQuestionsRepositoryImpl
import com.infinitepower.newquiz.data.repository.numbers.NumberTriviaQuestionApiImpl
import com.infinitepower.newquiz.data.repository.numbers.NumberTriviaQuestionRepositoryImpl
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.domain.repository.daily_challenge.DailyChallengeRepository
import com.infinitepower.newquiz.domain.repository.home.RecentCategoriesRepository
import com.infinitepower.newquiz.domain.repository.math_quiz.MathQuizCoreRepository
import com.infinitepower.newquiz.domain.repository.maze.MazeQuizRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.CountryCapitalFlagsQuizRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.FlagQuizRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.GuessMathSolutionRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.LogoQuizRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.MultiChoiceQuestionRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.saved_questions.SavedMultiChoiceQuestionsRepository
import com.infinitepower.newquiz.domain.repository.numbers.NumberTriviaQuestionApi
import com.infinitepower.newquiz.domain.repository.numbers.NumberTriviaQuestionRepository
import com.infinitepower.newquiz.domain.repository.wordle.WordleRepository
import com.infinitepower.newquiz.model.config.RemoteConfigApi
import com.infinitepower.newquiz.wordle.data.repository.remote_config.TestFirebaseRemoteConfigApiImpl
import com.infinitepower.newquiz.wordle.data.repository.wordle.FakeWordleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class TestRepositoryModule {
    @Binds
    abstract fun bindWordleRepository(wordleRepository: FakeWordleRepository): WordleRepository

    @Binds
    abstract fun bindMultiChoiceQuestionRepository(openTDBRepository: MultiChoiceQuestionRepositoryImpl): MultiChoiceQuestionRepository

    @Binds
    abstract fun bindSavedMultiChoiceQuestionsRepository(savedQuestionsRepository: SavedMultiChoiceQuestionsRepositoryImpl): SavedMultiChoiceQuestionsRepository

    @Binds
    abstract fun bindFlagQuizRepository(flagQuizRepositoryImpl: FlagQuizRepositoryImpl): FlagQuizRepository

    @Binds
    abstract fun bindLogoQuizRepository(logoQuizRepositoryImpl: LogoQuizRepositoryImpl): LogoQuizRepository

    @Binds
    abstract fun bindMathQuizCoreRepository(mathQuizCoreRepositoryImpl: MathQuizCoreRepositoryImpl): MathQuizCoreRepository

    @Binds
    abstract fun bindMazeMathQuizRepository(mazeMathQuizRepository: MazeQuizRepositoryImpl): MazeQuizRepository

    @Binds
    abstract fun bindGuessMathSolutionRepository(guessMathSolutionRepository: GuessMathSolutionRepositoryImpl): GuessMathSolutionRepository

    @Binds
    abstract fun bindNumberTriviaQuestionApi(numbersTriviaQuestionApiImpl: NumberTriviaQuestionApiImpl): NumberTriviaQuestionApi

    @Binds
    abstract fun bindNumberTriviaQuestionRepository(numberTriviaQuestionRepositoryImpl: NumberTriviaQuestionRepositoryImpl): NumberTriviaQuestionRepository

    @Binds
    abstract fun bindCountryCapitalFlagsQuizRepository(countryCapitalFlagsQuizRepository: CountryCapitalFlagsQuizRepositoryImpl): CountryCapitalFlagsQuizRepository

    @Binds
    abstract fun bindComparisonQuizRepository(comparisonQuizRepository: ComparisonQuizRepositoryImpl): ComparisonQuizRepository

    @Binds
    abstract fun bindDailyChallengeRepository(dailyChallengeRepository: DailyChallengeRepositoryImpl): DailyChallengeRepository

    @Binds
    abstract fun bindRemoteConfigApi(impl: TestFirebaseRemoteConfigApiImpl): RemoteConfigApi

    @Binds
    abstract fun bindRecentCategoriesRepository(impl: RecentCategoriesRepositoryImpl): RecentCategoriesRepository
}