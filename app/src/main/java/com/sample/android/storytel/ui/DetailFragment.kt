package com.sample.android.storytel.ui

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
import com.sample.android.storytel.BR
import com.sample.android.storytel.R
import com.sample.android.storytel.databinding.FragmentDetailBinding
import com.sample.android.storytel.util.Resource
import com.sample.android.storytel.viewmodels.DetailViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class DetailFragment @Inject
constructor() // Required empty public constructor
    : DaggerFragment() {

    @Inject
    lateinit var factory: DetailViewModel.Factory

    private val viewModel: DetailViewModel by lazy {
        ViewModelProvider(this, factory).get(DetailViewModel::class.java)
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDetailBinding.inflate(inflater, container, false).apply {
            setVariable(BR.vm, viewModel)
            // Set the lifecycleOwner so DataBinding can observe LiveData
            lifecycleOwner = viewLifecycleOwner
            post = factory.post
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