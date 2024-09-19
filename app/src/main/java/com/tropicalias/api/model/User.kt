package com.tropicalias.api.model

import java.util.Date

data class User(
    val firebaseId: String,
    val username: String,
    val email: String,
    val senha: String,
    val id: Long?,
    val descricaoUsuario: String?,
    val cpf: String?,
    val nome: String?,
    val nascimento: Date?,
    val urlFoto: String?,
    val createdAt: Long?,
    val deletedAt: Long?
) {
    constructor(firebaseId: String, username: String, email: String, senha: String) : this(
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
        null,
        null
    )
}