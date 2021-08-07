package com.sample.android.storytel.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.android.storytel.domain.Comment
import com.sample.android.storytel.domain.Post
import com.sample.android.storytel.network.StorytelService
import com.sample.android.storytel.util.schedulars.BaseSchedulerProvider
import javax.inject.Inject

class DetailViewModel(
    api: StorytelService,
    schedulerProvider: BaseSchedulerProvider,
    post: Post,
) : BaseDetailViewModel<List<Comment>>
    (schedulerProvider) {

    init {
        sendRequest(api.getComments(post.id))
    }

    /**
     * Factory for constructing DetailViewModel with parameter
     */
    class Factory @Inject constructor(
        private val api: StorytelService,
        private val schedulerProvider: BaseSchedulerProvider,
        private val post: Post,
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