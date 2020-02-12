package com.sample.android.storytel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sample.android.storytel.domain.Comment
import com.sample.android.storytel.domain.Photo
import com.sample.android.storytel.domain.Post
import com.sample.android.storytel.network.StorytelService
import com.sample.android.storytel.usecase.DetailUseCase
import com.sample.android.storytel.util.schedulars.BaseSchedulerProvider
import com.sample.android.storytel.util.schedulars.ImmediateSchedulerProvider
import com.sample.android.storytel.viewmodels.DetailViewModel
import io.reactivex.Observable
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
    private lateinit var api: StorytelService

    private lateinit var schedulerProvider: BaseSchedulerProvider
    private lateinit var useCase: DetailUseCase

    private lateinit var post: Post

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        // Make the sure that all schedulers are immediate.
        schedulerProvider = ImmediateSchedulerProvider()
        useCase = DetailUseCase(schedulerProvider, api)

        post = Post(
            1, 1, "title", "body",
            Photo(1, 1, "title", "url", "thumbnailUrl")
        )
    }

    @Test
    fun loadComments() {
        val comment = Comment(1, 1, "name", "email", "body")
        val observableResponse = Observable.just(listOf(comment))
        `when`(api.getComments(anyInt())).thenReturn(observableResponse)

        val viewModel = DetailViewModel(useCase, post)

        with(viewModel) {

            assertFalse(comments.value!!.isEmpty())
            assertTrue(comments.value?.size == 1)
        }
    }

    @Test
    fun errorLoadingComments() {
        val observableResponse = Observable.error<List<Comment>>(Exception())
        `when`(api.getComments(anyInt())).thenReturn(observableResponse)

        val viewModel = DetailViewModel(useCase, post)

        with(viewModel) {
            assertTrue(comments.value == null)
        }
    }
}