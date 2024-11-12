package com.tropicalias.ui.posts.newpost

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.storage.FirebaseStorage
import com.tropicalias.MainActivity
import com.tropicalias.R
import com.tropicalias.api.model.Post
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.utils.ApiHelper
import java.util.UUID

class NewPostService : Service() {

    private val noSQLApi = ApiRepository.getInstance().getNoSQL()
    private var posting = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val content = intent?.extras?.getString("content")
        val imageUrl = intent?.extras?.get("imageUri") as Uri?
        if (content != null) {
            savePost(content, imageUrl)
        }
        return START_NOT_STICKY
    }

    private fun savePost(content: String, imageUri: Uri?) {
        if (posting) {
            Log.d("NewPostService", "Post is already in progress, skipping duplicate request.")
            return
        }
        posting = true

        ApiHelper.getUser { user ->
            if (imageUri != null) {
                val storage = FirebaseStorage.getInstance()
                val storageRef = storage.reference.child("post/${UUID.randomUUID()}.jpg")
                FirebaseStorage.getInstance().maxUploadRetryTimeMillis = 5000L
                storageRef.putFile(imageUri)
                    .addOnSuccessListener {
                        storageRef.downloadUrl.addOnSuccessListener { imageUri ->
                            val post = Post(
                                userId = user.id!!,
                                userImage = user.imageUri,
                                userName = user.exibitionName ?: user.username,
                                media = imageUri,
                                content = content,
                            )
                            createPost(post)
                        }
                    }
                    .addOnFailureListener {
                        posting = false
                        Log.d("NewPostService", "savePost: $it")
                        Toast.makeText(
                            this.applicationContext,
                            "Erro ao fazer publicação: tente novamente mais tarde",
                            Toast.LENGTH_SHORT
                        ).show()
                        stopSelf() // Stop service on failure
                    }
            } else {
                val post = Post(
                    userId = user.id!!,
                    userImage = user.imageUri,
                    userName = user.exibitionName ?: user.username,
                    media = null,
                    content = content,
                )
                createPost(post)
            }
        }
    }

    private fun createPost(post: Post) {
        noSQLApi.createPost(post).enqueue(object : retrofit2.Callback<Post> {
            override fun onResponse(
                call: retrofit2.Call<Post>,
                response: retrofit2.Response<Post>
            ) {
                if (response.isSuccessful) {
                    Log.d("NewPostService", "Post created successfully.")
                    sendNotification("Publicação enviada", "Sua publicação foi criada com sucesso.")
                } else {
                    Log.e(
                        "NewPostService",
                        "Post creation failed with error code: ${response.code()}"
                    )
                }
                posting = false
                stopSelf() // Stop service after completion
            }

            override fun onFailure(call: retrofit2.Call<Post>, t: Throwable) {
                Toast.makeText(this@NewPostService, "A criação do post falhou", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun sendNotification(title: String, message: String) {
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            Intent(applicationContext, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "post_notification_channel"
        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.araci)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        createNotificationChannel(channelId)
        val notificationManager = NotificationManagerCompat.from(applicationContext)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(1, builder.build())
        }
    }

    private fun createNotificationChannel(channelId: String) {
        val manager = getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(
            channelId,
            "Post Notifications",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notifications for post creation status"
        }
        manager.createNotificationChannel(channel)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
