package com.sample.android.storytel.usecase

import com.sample.android.storytel.domain.Comment
import com.sample.android.storytel.network.StorytelService
import com.sample.android.storytel.util.schedulars.BaseSchedulerProvider
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailUseCase @Inject constructor(
    schedulerProvider: BaseSchedulerProvider,
    private val api : StorytelService
) : BaseUseCase(schedulerProvider) {

    fun getComments(id : Int) : Observable<List<Comment>> = composeObservable { api.getComments(id) }

}