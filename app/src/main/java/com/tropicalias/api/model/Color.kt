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

    constructor() : this(
        0, "006996", "E45F15", "32B6F4"
    )

    val colorPrimaryHex: String
        get() {
            if (colorPrimary[0] == '#') {
                return colorPrimary
            }
            return "#$colorPrimary"
        }

    val colorSecondaryHex: String
        get() {
            if (colorSecondary[0] == '#') {
                return colorSecondary
            }
            return "#$colorSecondary"
        }

    val colorBackgroundHex: String
        get() {
            if (colorBackground[0] == '#') {
                return colorBackground
            }
            return "#$colorBackground"
        }
}

