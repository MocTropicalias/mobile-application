package com.tropicalias.ui.events.qrcode

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Surface
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.tropicalias.R
import com.tropicalias.api.Retrofit.ApiType.LANDING_PAGE
import com.tropicalias.databinding.ActivityQrCodeBinding
import com.tropicalias.ui.events.eventdetails.EventActivity
import com.tropicalias.utils.RequestPermission

class QrCodeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQrCodeBinding

    private lateinit var cameraProviderListenableFeature: ListenableFuture<ProcessCameraProvider>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityQrCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (RequestPermission.requestCameraPermission(applicationContext, this)) {
            init()
        } else {
            finish()
        }

        binding.backButton.setOnClickListener {
            finish()
        }


    }

    private var isFirstLaunch = true
    override fun onResume() {
        super.onResume()
        if (!isFirstLaunch) {
            finish()
        }
        isFirstLaunch = false
    }

    private fun init() {
        cameraProviderListenableFeature = ProcessCameraProvider.getInstance(applicationContext)
        cameraProviderListenableFeature.addListener({
            val cameraProvider = cameraProviderListenableFeature.get()
            var notOpening = true
            bindImageAnalysis(cameraProvider) { url ->
                Log.d("QRCODE", "url: $url")
                if (matchesUrl(url) && notOpening) {
                    notOpening = false
                    val intent = Intent(binding.root.context, EventActivity::class.java)

                    val uri = Uri.parse(url)
                    intent.data = uri
                    ContextCompat.startActivity(binding.root.context, intent, null)

                }
            }
        }, ContextCompat.getMainExecutor(applicationContext))
    }

    private fun matchesUrl(url: String): Boolean {
        val deepLinkPattern = Regex("^${LANDING_PAGE.url}/event.*$")
        return deepLinkPattern.matches(url)
    }

    @OptIn(ExperimentalGetImage::class)
    fun bindImageAnalysis(
        processCameraProvider: ProcessCameraProvider,
        callback: (value: String) -> Unit
    ) {
        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetRotation(Surface.ROTATION_0)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(applicationContext)) { imageProxy ->
            val mediaImage = imageProxy.image

            if (mediaImage != null) {
                val image =
                    InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                val scanner = BarcodeScanning.getClient()
                val result = scanner.process(image)
                result.addOnSuccessListener { barcodes ->
                    barcodes.forEach { barcode ->
                        val getValue = barcode.rawValue
                        if (getValue != null) {
                            callback(getValue)
                        }
                    }
                    imageProxy.close()
                    mediaImage.close()
                }
            }
        }

        val preview = Preview.Builder().build()
        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

        preview.setSurfaceProvider(binding.previewView.surfaceProvider)
        processCameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview)

    }
}