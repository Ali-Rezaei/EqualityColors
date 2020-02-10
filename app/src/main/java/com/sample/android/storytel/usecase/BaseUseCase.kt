package com.sample.android.storytel.usecase

import com.sample.android.storytel.util.EspressoIdlingResource
import com.sample.android.storytel.util.schedulars.BaseSchedulerProvider
import io.reactivex.Observable

open class BaseUseCase(private val schedulerProvider: BaseSchedulerProvider) {

    protected fun <T> composeObservable(task: () -> Observable<T>): Observable<T> = task()
        .doOnSubscribe { EspressoIdlingResource.increment() } // App is busy until further notice
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.ui())
        .doFinally {
            if (!EspressoIdlingResource.countingIdlingResource.isIdleNow) {
                EspressoIdlingResource.decrement() // Set app as idle.
            }
        }
}