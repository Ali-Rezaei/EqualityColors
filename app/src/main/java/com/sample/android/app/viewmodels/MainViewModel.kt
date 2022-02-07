package com.sample.android.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.android.app.domain.Post
import com.sample.android.app.network.*
import com.sample.android.app.util.Resource
import com.sample.android.app.util.schedulars.BaseSchedulerProvider
import timber.log.Timber
import javax.inject.Inject


/**
 * MainViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 */
class MainViewModel(
    private val api: ApiService,
    schedulerProvider: BaseSchedulerProvider,
) : BaseViewModel<List<Post>>(schedulerProvider) {

    init {
        sendRequest()
    }

    fun sendRequest() {
        mutableLiveData.value = Resource.Loading()
        val requestWrapper = RequestWrapper()
        composeSingle {
            api.getPhotos().map { requestWrapper.networkPhotos = it }
                .flatMap { api.getPosts().map { requestWrapper.networkPosts = it } }
        }.subscribe({
            mutableLiveData.postValue(Resource.Success(
                requestWrapper.networkPosts?.let { networkPosts ->
                    requestWrapper.networkPhotos?.let { networkPhotos ->
                        PostsAndImages(networkPosts, networkPhotos).asDomainModel()
                    }
                }
            ))
        }) {
            mutableLiveData.postValue(Resource.Failure(it.localizedMessage))
            Timber.e(it)
        }.also { compositeDisposable.add(it) }
    }

    class RequestWrapper(
        var networkPhotos: List<NetworkPhoto>? = null,
        var networkPosts: List<NetworkPost>? = null,
    )

    /**
     * Factory for constructing MainViewModel with parameter
     */
    class Factory @Inject constructor(
        private val api: ApiService,
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