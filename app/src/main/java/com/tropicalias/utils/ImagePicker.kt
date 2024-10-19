package com.tropicalias.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.util.UUID

class ImagePicker {
    companion object {
        fun getChoserIntent(context: Context): Pair<Intent, Uri> {
            val galleryIntent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
            val imageFile = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "profile_picture_${UUID.randomUUID()}.jpg"
            )
            val imageUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                imageFile
            )
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            }
            val chooserIntent =
                Intent.createChooser(galleryIntent, "Selecione sua foto de perfil").apply {
                    putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
                }
            return Pair(chooserIntent, imageUri)
        }


    }
}