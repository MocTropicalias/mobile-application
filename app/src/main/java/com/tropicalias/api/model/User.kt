package com.tropicalias.api.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.Date

data class User(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("firebaseId")
    val firebaseId: String,
    @SerializedName("username")
    var username: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("nome")
    var exibitionName: String?,
    @SerializedName("cpf")
    var cpf: String?,
    @SerializedName("descricaoUsuario")
    var userDescription: String?,
    @SerializedName("nascimento")
    var birthDate: Date?,
    @SerializedName("urlFoto")
    var imageUri: Uri?,
    @SerializedName("deletedAt")
    var deletedAt: Date?,
    @SerializedName("createdAt")
    var createdAt: Date?,
    @SerializedName("qtSeguidores")
    var followersCount: Int?,
    @SerializedName("qtSeguidos")
    var followingCount: Int?,
    var userRole: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        TODO("birthDate"),
        parcel.readParcelable(Uri::class.java.classLoader),
        TODO("deletedAt"),
        TODO("createdAt"),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString().toString()
    )

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
        null,
        null,
        null,
        ""
    )

    constructor(displayName: String, imageUri: Uri?) :
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
                imageUri,
                null,
                null,
                null,
                null,
                ""
            )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(firebaseId)
        parcel.writeString(username)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(exibitionName)
        parcel.writeString(cpf)
        parcel.writeString(userDescription)
        parcel.writeParcelable(imageUri, flags)
        parcel.writeValue(followersCount)
        parcel.writeValue(followingCount)
        parcel.writeValue(userRole)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}