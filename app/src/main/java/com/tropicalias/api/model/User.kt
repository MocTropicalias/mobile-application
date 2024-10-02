package com.tropicalias.api.model

import android.net.Uri
import com.google.gson.annotations.SerializedName
import java.util.Date

data class User(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("firebaseId")
    val firebaseId: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("nome")
    val exibitionName: String?,
    @SerializedName("cpf")
    val cpf: String?,
    @SerializedName("descricaoUsuario")
    val userDescription: String?,
    @SerializedName("nascimento")
    val birthDate: Date?,
    @SerializedName("urlFoto")
    val photoUrl: Uri?,
    @SerializedName("deletedAt")
    val deletedAt: Date?,
    @SerializedName("createdAt")
    val createdAt: Date?,
) {
    constructor(firebaseId: String, username: String, email: String, senha: String) : this(
        null,
        firebaseId,
        username,
        email,
        senha,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )

    constructor(displayName: String, photoUrl: Uri?) :
            this(
                null,
                null.toString(),
                displayName,
                "",
                "",
                null,
                null,
                null,
                null,
                photoUrl,
                null,
                null,
            )
}