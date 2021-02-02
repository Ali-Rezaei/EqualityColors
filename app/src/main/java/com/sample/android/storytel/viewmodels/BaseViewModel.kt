package com.sample.android.storytel.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sample.android.storytel.util.EspressoIdlingResource
import com.sample.android.storytel.util.Resource
import com.sample.android.storytel.util.schedulars.BaseSchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

abstract class BaseViewModel<T, R, K>(
        private val schedulerProvider: BaseSchedulerProvider,
        private val requestObservable: Pair<Observable<R>, K?>
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _liveData = MutableLiveData<Resource<T>>()
    val liveData: LiveData<Resource<T>>
        get() = _liveData

    protected abstract fun getSuccessResult(it: R, wrapper: K?): T?

    init {
        sendRequest()
    }

    fun sendRequest() {
        _liveData.postValue(Resource.Loading())
        composeObservable { requestObservable.first }.subscribe({
            _liveData.postValue(Resource.Success(getSuccessResult(it, requestObservable.second)))
        }) {
            _liveData.postValue(Resource.Failure(it.localizedMessage))
            Timber.e(it)
        }.also { compositeDisposable.add(it) }
    }

    private fun <T> composeObservable(task: () -> Observable<T>): Observable<T> = task()
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