package com.sample.android.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.android.app.domain.Comment
import com.sample.android.app.domain.Post
import com.sample.android.app.network.ApiService
import com.sample.android.app.util.Resource
import com.sample.android.app.util.schedulars.BaseSchedulerProvider
import timber.log.Timber
import javax.inject.Inject

class DetailViewModel(
    private val api: ApiService,
    private val post: Post,
    schedulerProvider: BaseSchedulerProvider
) : BaseViewModel(schedulerProvider) {

    private val _liveData = MutableLiveData<Resource<List<Comment>>>()
    val liveData: LiveData<Resource<List<Comment>>>
        get() = _liveData

    init {
        sendRequest()
    }

    private fun sendRequest() {
        _liveData.value = Resource.Loading()
        composeSingle { api.getComments(post.id) }.subscribe({
            _liveData.postValue(Resource.Success(it))
        }) {
            _liveData.postValue(Resource.Failure(it.localizedMessage))
            Timber.e(it)
        }.also { compositeDisposable.add(it) }
    }

    /**
     * Factory for constructing DetailViewModel with parameter
     */
    class Factory @Inject constructor(
        private val api: ApiService,
        private val post: Post,
        private val schedulerProvider: BaseSchedulerProvider
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(api, post, schedulerProvider) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}