package com.sample.android.storytel.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.android.storytel.domain.Comment
import com.sample.android.storytel.domain.Post
import com.sample.android.storytel.network.StorytelService
import com.sample.android.storytel.util.schedulars.BaseSchedulerProvider
import timber.log.Timber
import javax.inject.Inject

class DetailViewModel(
    api: StorytelService,
    schedulerProvider: BaseSchedulerProvider,
    post: Post
) : BaseViewModel(schedulerProvider) {

    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>>
        get() = _comments

    init {
        composeObservable { api.getComments(post.id) }
            .subscribe({
                _comments.postValue(it)
            }) {
                Timber.e(it)
            }.also { compositeDisposable.add(it) }
    }

    /**
     * Factory for constructing DetailViewModel with parameter
     */
    class Factory @Inject constructor(
        private val api: StorytelService,
        private val schedulerProvider: BaseSchedulerProvider,
        val post: Post
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(api, schedulerProvider, post) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}