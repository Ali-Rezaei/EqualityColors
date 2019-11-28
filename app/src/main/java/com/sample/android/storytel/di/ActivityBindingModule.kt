package com.sample.android.storytel.di

import com.sample.android.storytel.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(
        modules = [BaseModule::class,
            MainModule::class,
            DetailModule::class]
    )
    internal abstract fun mainActivity(): MainActivity
}
