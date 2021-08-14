package com.sample.android.app

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.sample.android.app.domain.Post
import com.sample.android.app.network.NetworkPhoto
import com.sample.android.app.network.NetworkPost
import com.sample.android.app.network.ApiService
import com.sample.android.app.util.Resource
import com.sample.android.app.util.schedulars.ImmediateSchedulerProvider
import com.sample.android.app.viewmodels.MainViewModel
import io.reactivex.Single
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var api: ApiService

    @Test
    fun loadPosts() {
        val networkPhoto1 = NetworkPhoto(1, 1, "title", "url", "thumbnailUrl")
        val networkPhoto2 = NetworkPhoto(1, 2, "title", "url", "thumbnailUrl")
        `when`(api.getPhotos()).thenReturn(Single.just(listOf(networkPhoto1, networkPhoto2)))

        val networkPost = NetworkPost(1, 1, "title", "body")
        `when`(api.getPosts()).thenReturn(Single.just(listOf(networkPost)))

        // Make the sure that all schedulers are immediate.
        val schedulerProvider = ImmediateSchedulerProvider()

        val viewModel = MainViewModel(api, schedulerProvider)

        val observer = LoggingObserver<Resource<List<Post>>>()
        viewModel.liveData.observeForever(observer)

        with(observer.value) {
            if (this is Resource.Success) {
                assertThat(this, `is`(notNullValue()))
                assertTrue(data!!.isNotEmpty())
                assertThat(data?.size, `is`(1))
            }
        }
    }

    /**
     * simple observer that logs the latest value it receives
     */
    private class LoggingObserver<T> : Observer<T> {
        var value: T? = null
        override fun onChanged(t: T?) {
            this.value = t
        }
    }
}
