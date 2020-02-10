package com.sample.android.storytel.di

import android.app.Application
import com.sample.android.storytel.StorytelApplication
import com.sample.android.storytel.network.Network

import javax.inject.Singleton

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Singleton
@Component(
    modules = [ActivityBindingModule::class,
        AndroidSupportInjectionModule::class,
        Network::class,
        BaseModule::class]
)
interface AppComponent : AndroidInjector<StorytelApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}