package com.tropicalias.api.model

import android.net.Uri

data class Event(
    var id: Long,
    var title: String,
    var eventImage: Uri,
    var date: Long,
    var local: String
)