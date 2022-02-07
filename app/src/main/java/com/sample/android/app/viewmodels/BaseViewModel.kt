package com.sample.android.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sample.android.app.util.EspressoIdlingResource
import com.sample.android.app.util.Resource
import com.sample.android.app.util.schedulars.BaseSchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel<T>(
    val schedulerProvider: BaseSchedulerProvider
) : ViewModel() {

    protected val compositeDisposable = CompositeDisposable()

    protected val mutableLiveData = MutableLiveData<Resource<T>>()
    val liveData: LiveData<Resource<T>>
        get() = mutableLiveData

    protected inline fun <T> composeSingle(task: () -> Single<T>): Single<T> = task()
        .doOnSubscribe { EspressoIdlingResource.increment() } // App is busy until further notice
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.ui())
        .doFinally {
            if (!EspressoIdlingResource.countingIdlingResource.isIdleNow) {
                EspressoIdlingResource.decrement() // Set app as idle.
            }
        }

    /**
     * Called when the ViewModel is dismantled.
     * At this point, we want to cancel all disposables;
     * otherwise we end up with processes that have nowhere to return to
     * using memory and resources.
     */
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}