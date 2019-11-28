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

class DetailViewModel(
    private val schedulerProvider: BaseSchedulerProvider,
    private val api : StorytelService,
    private val post: Post
) : BaseViewModel() {

    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>>
        get() = _comments

    init {
        showComments()
    }

    private fun showComments() {
        compositeDisposable.add(api.getComments(post.id)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({
                _comments.postValue(it)
            }) {
                Timber.e(it)
            })
    }

    /**
     * Factory for constructing DetailViewModel with parameter
     */
    class Factory(
        private val schedulerProvider: BaseSchedulerProvider,
        private val api : StorytelService,
        private val post: Post
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(schedulerProvider, api, post) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}