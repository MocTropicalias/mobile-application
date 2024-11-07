package com.tropicalias.api.model

import android.net.Uri
import java.util.Date

data class Post(
    val id: String?,
    val userId: Long,
    var userPhoto: Uri?,
    var userName: String,
    val media: Uri?,
    val content: String,
    val likes: List<Long>,
    val comments: List<Comment>,
    val createdAt: Date?,
    var userLoaded: Boolean = false
) {
    constructor(
        userId: Long,
        userImage: Uri?,
        userName: String,
        media: Uri?,
        content: String,
    ) : this(
        null,
        userId,
        userImage,
        userName,
        media,
        content,
        emptyList(),
        emptyList(),
        null
    )
}
