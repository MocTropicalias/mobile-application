package com.tropicalias.api.model

import com.google.gson.annotations.SerializedName

data class Tent(
    val id: Long,
    @SerializedName("nome")
    val name: String,
    @SerializedName("numTickets")
    val ticketPrice: Int,
    @SerializedName("idEvento")
    val eventId: Long,
)
