package com.infinitepower.newquiz.core.di

import com.infinitepower.newquiz.core.util.ads.admob.rewarded.RewardedAdUtil
import com.infinitepower.newquiz.core.util.ads.admob.rewarded.RewardedAdUtilImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class RewardedAdModule {
    @Binds
    abstract fun bindRewardedAdUtil(rewardedAdUtilImpl: RewardedAdUtilImpl): RewardedAdUtil
}