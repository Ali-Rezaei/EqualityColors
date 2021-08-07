package com.sample.android.storytel.viewmodels

import com.sample.android.storytel.util.schedulars.BaseSchedulerProvider
import io.reactivex.Single

open class BaseDetailViewModel<T>(
    schedulerProvider: BaseSchedulerProvider
) : BaseViewModel<T, T, Nothing>(schedulerProvider) {

    override fun getSuccessResult(it: T, wrapper: Nothing?): T? = it

    protected fun sendRequest(requestSingle: Single<T>) {
        super.sendRequest(Pair(requestSingle, null))
    }
}