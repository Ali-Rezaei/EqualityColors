package com.sample.android.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.android.app.domain.Comment
import com.sample.android.app.domain.Post
import com.sample.android.app.network.ApiService
import com.sample.android.app.util.schedulars.BaseSchedulerProvider
import javax.inject.Inject

class DetailViewModel(
    api: ApiService,
    schedulerProvider: BaseSchedulerProvider,
    post: Post,
) : BaseDetailViewModel<List<Comment>>
    (schedulerProvider, api.getComments(post.id)) {

    /**
     * Factory for constructing DetailViewModel with parameter
     */
    class Factory @Inject constructor(
        private val api: ApiService,
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