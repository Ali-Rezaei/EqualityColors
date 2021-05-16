package com.sample.android.storytel.di

import androidx.lifecycle.ViewModelProvider
import com.sample.android.storytel.R
import com.sample.android.storytel.domain.Post
import com.sample.android.storytel.ui.DetailFragment
import com.sample.android.storytel.ui.DetailFragmentArgs
import com.sample.android.storytel.ui.MainActivity
import com.sample.android.storytel.viewmodels.DetailViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class DetailModule {

    @ContributesAndroidInjector
    internal abstract fun detailFragment(): DetailFragment

    @Binds
    internal abstract fun bindViewModelFactory(factory: DetailViewModel.Factory): ViewModelProvider.Factory

    @Module
    companion object {

        @Provides
        @JvmStatic
        internal fun providePost(activity: MainActivity): Post {
            val navHostFragment = activity.supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment)
            val fragment = navHostFragment?.childFragmentManager?.fragments?.get(0);
            return DetailFragmentArgs.fromBundle(fragment!!.requireArguments()).post
        }
    }
}