package com.sample.android.storytel.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.android.storytel.domain.Post
import com.sample.android.storytel.network.*
import com.sample.android.storytel.util.schedulars.BaseSchedulerProvider
import com.sample.android.storytel.viewmodels.MainViewModel.RequestWrapper
import io.reactivex.Single
import javax.inject.Inject


/**
 * MainViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 */
class MainViewModel(
        api: StorytelService,
        schedulerProvider: BaseSchedulerProvider,
) : BaseViewModel<List<Post>, Unit, RequestWrapper>(schedulerProvider,
        getPairRequestSingle(api.getPhotos(), api.getPosts())) {

    override fun getSuccessResult(it: Unit, wrapper: RequestWrapper?): List<Post>? =
            wrapper?.networkPosts?.let { networkPosts ->
                wrapper.networkPhotos?.let { networkPhotos ->
                    PostsAndImages(networkPosts, networkPhotos).asDomaineModel()
                }
            }

    class RequestWrapper(
            var networkPhotos: List<NetworkPhoto>? = null,
            var networkPosts: List<NetworkPost>? = null,
    )

    /**
     * Factory for constructing MainViewModel with parameter
     */
    class Factory @Inject constructor(
            private val api: StorytelService,
            private val schedulerProvider: BaseSchedulerProvider,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(api, schedulerProvider) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

fun getPairRequestSingle(
    networkPhotosSingle: Single<List<NetworkPhoto>>,
    networkPostsSingle: Single<List<NetworkPost>>,
): Pair<Single<Unit>, RequestWrapper> {
    val requestWrapper = RequestWrapper()
    val requestObservable = networkPhotosSingle.map { requestWrapper.networkPhotos = it }
            .flatMap { networkPostsSingle.map { requestWrapper.networkPosts = it } }
    return Pair(requestObservable, requestWrapper)
}