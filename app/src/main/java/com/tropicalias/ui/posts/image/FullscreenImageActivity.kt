package com.tropicalias.ui.posts.image

import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.tropicalias.databinding.ActivityFullscreenImageBinding

class FullscreenImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullscreenImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullscreenImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutParams = window.attributes
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height =
            WindowManager.LayoutParams.WRAP_CONTENT // Or MATCH_PARENT if full height is also needed
        window.attributes = layoutParams


        // Load image URL
        val imageUrl = intent.getParcelableExtra<Uri>("IMAGE_URL")
        Glide.with(this).load(imageUrl).into(binding.imageView)

        binding.zoomImageView.setOnTouchListener { _, event ->
            if (binding.zoomImageView.zoom <= 1f) {
                handleDragToDismiss(event)
                true
            }
            false
        }


    }

    private var startY: Float = 0f
    private val threshold = 300 // Adjust as needed for the drag distance


    private fun handleDragToDismiss(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startY = event.rawY // Record the starting Y position
            }

            MotionEvent.ACTION_MOVE -> {
                val currentY = event.rawY
                val dragDistance = currentY - startY // Calculate drag distance

                // Apply any visual feedback like transparency if desired
                binding.zoomImageView.alpha = 1 - (dragDistance / threshold).coerceIn(0f, 1f)

                if (dragDistance > threshold) {
                    finish() // Close activity if dragged far enough
                    overridePendingTransition(0, android.R.anim.fade_out) // Optional fade animation
                }
            }

            MotionEvent.ACTION_UP -> {
                // Reset alpha if not dismissed
                binding.zoomImageView.alpha = 1f
            }
        }
    }


}
