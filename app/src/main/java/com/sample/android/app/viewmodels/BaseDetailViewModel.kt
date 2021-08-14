package com.sample.android.app.viewmodels

import com.sample.android.app.util.schedulars.BaseSchedulerProvider
import io.reactivex.Single

open class BaseDetailViewModel<T>(
    schedulerProvider: BaseSchedulerProvider,
    requestSingle : Single<T>
) : BaseViewModel<T, T, Nothing>(schedulerProvider, Pair(requestSingle, null)) {

    override fun getSuccessResult(it: T, wrapper: Nothing?): T? = it
}