package com.sample.android.storytel.usecase

import com.sample.android.storytel.network.NetworkPhoto
import com.sample.android.storytel.network.NetworkPost
import com.sample.android.storytel.network.StorytelService
import com.sample.android.storytel.util.schedulars.BaseSchedulerProvider
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainUseCase @Inject constructor(
    schedulerProvider: BaseSchedulerProvider,
    private val api : StorytelService
    ) : BaseUseCase(schedulerProvider) {

    fun getPhotos() : Observable<List<NetworkPhoto>> = composeObservable { api.getPhotos() }

    fun getPost() : Observable<List<NetworkPost>> = composeObservable { api.getPosts() }
}