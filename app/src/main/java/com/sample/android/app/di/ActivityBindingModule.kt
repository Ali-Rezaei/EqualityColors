package com.sample.android.app.di

import com.sample.android.app.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(
        modules = [MainModule::class,
            DetailModule::class]
    )
    internal abstract fun mainActivity(): MainActivity
}
