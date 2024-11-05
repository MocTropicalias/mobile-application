package com.tropicalias.api.model

import android.net.Uri
import com.google.gson.annotations.SerializedName
import java.util.Date

data class Event(
    var id: Long,
    @SerializedName("nome")
    var title: String,
    @SerializedName("descricao")
    var description: String,
    @SerializedName("imagem")
    var image: Uri,
    var local: String,
    @SerializedName("precoTicket")
    var ticketPricing: Int,
    @SerializedName("dataInicio")
    var startDate: Date,
    @SerializedName("dataFinal")
    var endDate: Date,
    @SerializedName("idUsuario")
    var ownerId: Long,
    @SerializedName("barracas")
    var barracas: List<Tent>

)