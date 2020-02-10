package com.sample.android.storytel.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.postDelayed
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.sample.android.storytel.BR
import com.sample.android.storytel.R
import com.sample.android.storytel.databinding.FragmentDetailBinding
import com.sample.android.storytel.usecase.UseCase
import com.sample.android.storytel.viewmodels.DetailViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class DetailFragment @Inject
constructor() // Required empty public constructor
    : DaggerFragment() {

    @Inject
    lateinit var useCase: UseCase

    private lateinit var factory: DetailViewModel.Factory

    private val viewModel: DetailViewModel by lazy {
        ViewModelProviders.of(this, factory)
            .get(DetailViewModel::class.java)
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args = DetailFragmentArgs.fromBundle(arguments!!)
        factory = DetailViewModel.Factory(useCase, args.post)
        val binding = FragmentDetailBinding.inflate(inflater, container, false).apply {
            setVariable(BR.vm, viewModel)
            // Set the lifecycleOwner so DataBinding can observe LiveData
            lifecycleOwner = viewLifecycleOwner
            post = args.post
        }

        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.move)

        with(binding) {
            toolbar.apply {
                setNavigationOnClickListener { findNavController().navigateUp() }
            }
        }

        // Make sure we don't wait longer than a second for the Picasso image
        handler.postDelayed(1000) {
            startPostponedEnterTransition()
        }
        postponeEnterTransition()

        return binding.root
    }
}