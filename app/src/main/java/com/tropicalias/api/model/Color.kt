package com.tropicalias.api.model

import com.google.gson.annotations.SerializedName

data class Color(
    val id: Long,
    @SerializedName("corPrimaria")
    val colorPrimary: String,
    @SerializedName("corSecundaria")
    val colorSecondary: String,
    @SerializedName("corFundo")
    val colorBackground: String
) {

    val colorPrimaryHex: String get() = "#$colorPrimary"

    val colorSecondaryHex: String get() = "#$colorSecondary"

    val colorBackgroundHex: String get() = "#$colorBackground"
}

