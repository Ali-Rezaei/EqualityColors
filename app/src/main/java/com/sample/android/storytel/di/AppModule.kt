package com.sample.android.storytel.di

import com.sample.android.storytel.util.schedulars.BaseSchedulerProvider
import com.sample.android.storytel.util.schedulars.SchedulerProvider
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {

    @Binds
    internal abstract fun bindSchedulerProvider(schedulerProvider: SchedulerProvider): BaseSchedulerProvider
}