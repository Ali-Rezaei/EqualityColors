package com.sample.android.storytel.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.android.storytel.domain.Post
import com.sample.android.storytel.network.NetworkPhoto
import com.sample.android.storytel.network.NetworkPost
import com.sample.android.storytel.network.PostAndImages
import com.sample.android.storytel.network.asDomaineModel
import com.sample.android.storytel.usecase.MainUseCase
import com.sample.android.storytel.util.Resource
import timber.log.Timber
import javax.inject.Inject

/**
 * MainViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 */
class MainViewModel(
    private val useCase: MainUseCase
) : BaseViewModel() {

    private val _liveData = MutableLiveData<Resource<List<Post>>>()
    val liveData: LiveData<Resource<List<Post>>>
        get() = _liveData

    init {
        showItems()
    }

    fun showItems() {
        _liveData.postValue(Resource.Loading())
        val requestWrapper = RequestWrapper()
        useCase.getPhotos().map { requestWrapper.networkPhotos = it }
            .flatMap { useCase.getPost() }.map { requestWrapper.networkPosts = it }
            .subscribe({
                _liveData.postValue(Resource.Success(requestWrapper.networkPosts?.let { networkPost ->
                    requestWrapper.networkPhotos?.let { networkPhotos ->
                        PostAndImages(networkPost, networkPhotos).asDomaineModel()
                    }
                }))
            }) {
                _liveData.postValue(Resource.Failure(it.localizedMessage))
                Timber.e(it)
            }.also { compositeDisposable.add(it) }
    }

    class RequestWrapper(
        var networkPhotos: List<NetworkPhoto>? = null,
        var networkPosts: List<NetworkPost>? = null
    )

    /**
     * Factory for constructing MainViewModel with parameter
     */
    class Factory @Inject constructor(
        private val useCase: MainUseCase
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(useCase) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}