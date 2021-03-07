package com.sample.android.storytel.viewmodels

import com.sample.android.storytel.util.schedulars.BaseSchedulerProvider
import io.reactivex.Single

open class BasePostViewModel<T, R : T>(
        schedulerProvider: BaseSchedulerProvider,
        requestSingle: Single<R>,
) : BaseViewModel<T, R, Nothing>(schedulerProvider, Pair(requestSingle, null)) {

    override fun getSuccessResult(it: R, wrapper: Nothing?): T? = it
}