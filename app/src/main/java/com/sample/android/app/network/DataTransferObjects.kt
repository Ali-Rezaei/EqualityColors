package com.sample.android.app.network

import com.sample.android.app.domain.Photo
import com.sample.android.app.domain.Post
import java.util.Random

class PostsAndImages(
    val networkPosts: List<NetworkPost>,
    val networkPhotos: List<NetworkPhoto>
)

class NetworkPost(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

class NetworkPhoto(
    val albumId: Int,
    val id: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String
)

fun PostsAndImages.asDomainModel(): List<Post> {
    val posts = ArrayList<Post>()
    for (i in networkPosts.indices) {
        val networkPost = networkPosts[i]
        val networkPhoto = networkPhotos[Random().nextInt(networkPhotos.size - 1)]
        posts.add(
            Post(
                userId = networkPost.userId,
                id = networkPost.id,
                title = networkPost.title,
                body = networkPost.body,
                photo = Photo(
                    albumId = networkPhoto.albumId,
                    id = networkPhoto.id,
                    title = networkPhoto.title,
                    url = networkPhoto.url,
                    thumbnailUrl = networkPhoto.thumbnailUrl
                )
            )
        )
    }
    return posts
}