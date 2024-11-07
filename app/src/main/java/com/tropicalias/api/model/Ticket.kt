package com.tropicalias.api.model

import com.google.gson.annotations.SerializedName

data class Ticket(
    val id: Long,
    @SerializedName("quantidade")
    val amount: Int,
    @SerializedName("idUsuario")
    val userId: Long,
    @SerializedName("evento")
    val event: Event
)