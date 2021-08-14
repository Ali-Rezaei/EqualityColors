package com.sample.android.app.di

import com.sample.android.app.util.schedulars.BaseSchedulerProvider
import com.sample.android.app.util.schedulars.SchedulerProvider
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {

    @Binds
    internal abstract fun bindSchedulerProvider(schedulerProvider: SchedulerProvider): BaseSchedulerProvider
}