package com.sample.android.app

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sample.android.app.domain.Comment
import com.sample.android.app.domain.Photo
import com.sample.android.app.domain.Post
import com.sample.android.app.network.ApiService
import com.sample.android.app.util.Resource
import com.sample.android.app.util.schedulars.BaseSchedulerProvider
import com.sample.android.app.util.schedulars.ImmediateSchedulerProvider
import com.sample.android.app.viewmodels.DetailViewModel
import io.reactivex.Single
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class DetailViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var api: ApiService

    private lateinit var schedulerProvider: BaseSchedulerProvider

    private lateinit var post: Post

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        // Make the sure that all schedulers are immediate.
        schedulerProvider = ImmediateSchedulerProvider()

        post = Post(
                1, 1, "title", "body",
                Photo(1, 1, "title", "url", "thumbnailUrl")
        )
    }

    @Test
    fun loadComments() {
        val comment = Comment(1, 1, "name", "email", "body")
        val observableResponse = Single.just(listOf(comment))
        `when`(api.getComments(anyInt())).thenReturn(observableResponse)

        val viewModel = DetailViewModel(api, schedulerProvider, post)

        viewModel.liveData.value.let {
            if (it is Resource.Success) {
                assertFalse(it.data.isNullOrEmpty())
                assertTrue(it.data?.size == 1)
            }
        }
    }

    @Test
    fun errorLoadingComments() {
        val errorMessage = "Error"
        val observableResponse = Single.error<List<Comment>>(Exception(errorMessage))
        `when`(api.getComments(anyInt())).thenReturn(observableResponse)

        val viewModel = DetailViewModel(api, schedulerProvider, post)

        viewModel.liveData.value.let {
            if (it is Resource.Failure) {
                assertFalse(it.cause.isNullOrEmpty())
                assertTrue(it.cause == errorMessage)
            }
        }
    }
}