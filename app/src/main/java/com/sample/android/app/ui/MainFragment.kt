package com.sample.android.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.sample.android.app.BR
import com.sample.android.app.R
import com.sample.android.app.databinding.FragmentMainBinding
import com.sample.android.app.util.Resource
import com.sample.android.app.util.setupActionBar
import com.sample.android.app.viewmodels.MainViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class MainFragment @Inject
constructor() // Required empty public constructor
    : DaggerFragment() {

    @Inject
    lateinit var factory: MainViewModel.Factory

    /**
     * RecyclerView Adapter for converting a list of posts to cards.
     */
    private lateinit var viewModelAdapter: MainAdapter

    private var binding: FragmentMainBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding?.let {
            // execute this block if not null
            return it.root
        } ?: run {
            // execute this block if null
            val viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

            binding = FragmentMainBinding.inflate(inflater, container, false).apply {
                setVariable(BR.vm, viewModel)
                // Set the lifecycleOwner so DataBinding can observe LiveData
                lifecycleOwner = viewLifecycleOwner
            }

            viewModel.liveData.observe(viewLifecycleOwner, {
                if (it is Resource.Success)
                    viewModelAdapter.submitList(it.data)
            })

            viewModelAdapter =
                MainAdapter(MainAdapter.OnClickListener { post, imageView ->
                    val extras = FragmentNavigatorExtras(
                        imageView to post.id.toString()
                    )
                    val destination = MainFragmentDirections.actionMainFragmentToDetailFragment(post)
                    with(findNavController()) {
                        currentDestination?.getAction(destination.actionId)?.let { navigate(destination, extras) }
                    }

                })

            with(binding!!) {
                recyclerView.apply {
                    setHasFixedSize(true)
                    adapter = viewModelAdapter
                    postponeEnterTransition()
                    viewTreeObserver.addOnPreDrawListener {
                        startPostponedEnterTransition()
                        true
                    }
                }

                (activity as AppCompatActivity).setupActionBar(toolbar) {
                    setTitle(R.string.app_name)
                }
            }
            return binding?.root
        }
    }
}