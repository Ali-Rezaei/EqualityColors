package com.sample.android.storytel.util

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.sample.android.storytel.R
import com.squareup.picasso.Picasso

@BindingAdapter("showLoading")
fun View.showLoading(resource: Resource<*>?) {
    visibility = if (resource is Resource.Loading) View.VISIBLE else View.GONE
}


@BindingAdapter("showError")
fun View.showError(resource: Resource<*>?) {
    visibility = if (resource is Resource.Failure) View.VISIBLE else View.GONE
}

@BindingAdapter("showData")
fun View.showData(resource: Resource<*>?) {
    visibility = if (resource is Resource.Success) View.VISIBLE else View.GONE
}

/**
 * Binding adapter used to display images from URL using Picasso
 */
@BindingAdapter("imageUrl")
fun bindImage(layout: FrameLayout, url: String?) {
    val imageView: ImageView = layout.findViewById(R.id.image)
    Picasso.with(layout.context).load(url).into(imageView)
}

@BindingAdapter("imageUrl")
fun bindImage(imageView: ImageView, url: String?) {
    Picasso.with(imageView.context).load(url).into(imageView)
}