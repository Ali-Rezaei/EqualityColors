package com.sample.android.storytel.util

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.github.florent37.picassopalette.PicassoPalette
import com.sample.android.storytel.R
import com.squareup.picasso.Picasso

@BindingAdapter("showLoading")
fun View.showLoading(status: Status?) {
    visibility = if (status == Status.LOADING) View.VISIBLE else View.GONE
}


@BindingAdapter("showError")
fun View.showError(status: Status?) {
    visibility = if (status == Status.ERROR) View.VISIBLE else View.GONE
}

@BindingAdapter("showData")
fun View.showData(status: Status?) {
    visibility = if (status == Status.SUCCESS) View.VISIBLE else View.GONE
}

/**
 * Binding adapter used to display images from URL using Picasso
 */
@BindingAdapter("imageUrl")
fun bindImage(layout: FrameLayout, url: String?) {

    val imageView: ImageView = layout.findViewById(R.id.image)
    Picasso.with(layout.context).load(url).into(
        imageView,
        PicassoPalette.with(url, imageView)
            .use(PicassoPalette.Profile.VIBRANT)
            .intoBackground(layout.findViewById(R.id.image_background), PicassoPalette.Swatch.RGB)
            .intoTextColor(layout.findViewById(R.id.image_title), PicassoPalette.Swatch.BODY_TEXT_COLOR)
    )
}

@BindingAdapter("imageUrl")
fun bindImage(imageView: ImageView, url: String?) {
    Picasso.with(imageView.context).load(url).into(imageView)
}