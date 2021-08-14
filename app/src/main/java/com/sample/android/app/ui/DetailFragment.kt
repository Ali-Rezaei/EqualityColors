package com.sample.android.app.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.postDelayed
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.sample.android.app.BR
import com.sample.android.app.R
import com.sample.android.app.databinding.FragmentDetailBinding
import com.sample.android.app.domain.Post
import com.sample.android.app.util.Resource
import com.sample.android.app.viewmodels.DetailViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class DetailFragment @Inject
constructor() // Required empty public constructor
    : DaggerFragment() {

    @Inject
    lateinit var factory: DetailViewModel.Factory

    @Inject
    lateinit var post: Post

    private val viewModel: DetailViewModel by lazy {
        ViewModelProvider(this, factory).get(DetailViewModel::class.java)
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentDetailBinding.inflate(inflater, container, false).apply {
            setVariable(BR.vm, viewModel)
            // Set the lifecycleOwner so DataBinding can observe LiveData
            lifecycleOwner = viewLifecycleOwner
            post = this@DetailFragment.post
        }

        sharedElementEnterTransition =
                TransitionInflater.from(context).inflateTransition(R.transition.move)

        with(binding) {
            toolbar.apply {
                setNavigationOnClickListener { findNavController().navigateUp() }
            }
            viewModel.liveData.observe(viewLifecycleOwner, { resource ->
                if (resource is Resource.Success) {
                    comments = resource.data
                }
            })
        }

        // Make sure we don't wait longer than a second for the Picasso image
        handler.postDelayed(1000) {
            startPostponedEnterTransition()
        }
        postponeEnterTransition()

        return binding.root
    }
}