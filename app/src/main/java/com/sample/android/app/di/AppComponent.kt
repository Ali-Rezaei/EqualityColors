package com.sample.android.app.di

import com.sample.android.app.Application
import com.sample.android.app.network.Network

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
        AppModule::class]
)
interface AppComponent : AndroidInjector<Application> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: android.app.Application): Builder

        fun build(): AppComponent
    }
}