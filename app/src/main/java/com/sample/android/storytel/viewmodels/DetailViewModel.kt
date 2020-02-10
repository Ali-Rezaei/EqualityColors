package com.sample.android.storytel.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.android.storytel.domain.Comment
import com.sample.android.storytel.domain.Post
import com.sample.android.storytel.usecase.UseCase
import timber.log.Timber

class DetailViewModel(
    useCase: UseCase,
    post: Post
) : BaseViewModel() {

    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>>
        get() = _comments

    init {
        compositeDisposable.add(useCase.getComments(post.id)
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
        private val useCase: UseCase,
        private val post: Post
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(useCase, post) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}