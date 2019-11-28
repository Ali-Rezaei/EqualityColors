package com.sample.android.storytel.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String,
    val photo: Photo
) : Parcelable

@Parcelize
data class Photo(
    val albumId: Int,
    val id: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String
) : Parcelable

class Comment(
    val postId: Int,
    val id: Int,
    val name: String,
    val email: String,
    val body: String
)