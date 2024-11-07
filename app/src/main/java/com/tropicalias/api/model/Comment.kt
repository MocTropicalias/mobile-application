package com.tropicalias.api.model

import android.net.Uri
import com.google.gson.annotations.SerializedName
import java.util.Date

data class Comment(
    val userId: Long,
    val userImage: Uri?,
    val userName: String,
    val content: String,
    @SerializedName("createdAt")
    val date: Date?
)
