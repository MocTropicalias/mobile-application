package com.tropicalias.api.model

import com.google.gson.annotations.SerializedName

data class Mascote(
    val id: Long,

    @SerializedName("nome")
    val name: String,

    @SerializedName("usuarioId")
    val userId: Long,

    @SerializedName("corAraci")
    val colorScheme: Color
)
