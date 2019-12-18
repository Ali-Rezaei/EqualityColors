package com.sample.android.storytel.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.android.storytel.domain.Post
import com.sample.android.storytel.network.NetworkPhoto
import com.sample.android.storytel.network.PostAndImages
import com.sample.android.storytel.network.StorytelService
import com.sample.android.storytel.network.asDomaineModel
import com.sample.android.storytel.util.EspressoIdlingResource
import com.sample.android.storytel.util.Resource
import com.sample.android.storytel.util.schedulars.BaseSchedulerProvider
import timber.log.Timber
import javax.inject.Inject

/**
 * MainViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 */
class MainViewModel(
    private val schedulerProvider: BaseSchedulerProvider,
    private val api: StorytelService
) : BaseViewModel() {

    private val _liveData = MutableLiveData<Resource<List<Post>>>()
    val liveData: LiveData<Resource<List<Post>>>
        get() = _liveData

    init {
        showPhotos()
    }

    fun showPhotos() {
        EspressoIdlingResource.increment() // App is busy until further notice
        _liveData.postValue(Resource.Loading())
        compositeDisposable.add(api.getPhotos()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .doFinally {
                if (!EspressoIdlingResource.countingIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement() // Set app as idle.
                }
            }
            .subscribe({
                showPosts(it)
            }) {
                _liveData.postValue(Resource.Failure(it.localizedMessage))
                Timber.e(it)
            })
    }

    private fun showPosts(networkPhotos: List<NetworkPhoto>) {
        EspressoIdlingResource.increment() // App is busy until further notice
        _liveData.postValue(Resource.Loading())
        compositeDisposable.add(api.getPosts()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .doFinally {
                if (!EspressoIdlingResource.countingIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement() // Set app as idle.
                }
            }
            .subscribe({ networkPosts ->
                _liveData.postValue(
                    Resource.Success(PostAndImages(networkPosts, networkPhotos).asDomaineModel())
                )

            }) {
                _liveData.postValue(Resource.Failure(it.localizedMessage))
                Timber.e(it)
            })
    }

    /**
     * Factory for constructing MainViewModel with parameter
     */
    class Factory @Inject constructor(
        private val schedulerProvider: BaseSchedulerProvider,
        private val api: StorytelService
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(schedulerProvider, api) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}