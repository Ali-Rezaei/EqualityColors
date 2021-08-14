package com.sample.android.app.di

import android.app.Application
import com.sample.android.app.SampleApplication
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
interface AppComponent : AndroidInjector<SampleApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}