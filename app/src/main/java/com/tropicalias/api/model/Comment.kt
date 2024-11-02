package com.tropicalias.api.model

import android.net.Uri
import java.util.Date

data class Comment(
    val userId: Long,
    val userImage: Uri?,
    val userName: String,
    val content: String,
    val date: Date?
)
