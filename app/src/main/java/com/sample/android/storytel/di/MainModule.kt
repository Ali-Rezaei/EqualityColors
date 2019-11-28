package com.sample.android.storytel.di

import androidx.lifecycle.ViewModelProvider
import com.sample.android.storytel.ui.MainFragment
import com.sample.android.storytel.viewmodels.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainModule {

    @ContributesAndroidInjector
    internal abstract fun mainFragment(): MainFragment

    @Binds
    internal abstract fun bindViewModelFactory(factory: MainViewModel.Factory): ViewModelProvider.Factory
}