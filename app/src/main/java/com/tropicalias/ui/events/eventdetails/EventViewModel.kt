package com.tropicalias.ui.events.eventdetails


//import com.google.zxing.BarcodeFormat
//import com.google.zxing.WriterException
//import com.google.zxing.common.BitMatrix
//import com.google.zxing.pdf417.PDF417Writer
import android.app.Application
import androidx.lifecycle.AndroidViewModel

class EventViewModel(application: Application) : AndroidViewModel(application) {

//    private val imageDirectory = "${Environment.getExternalStorageDirectory()}/qrgenerator/users"
//    private val qrDirectory = "${Environment.getExternalStorageDirectory()}/qrgenerator"
//
//    var jsonData: JSONObject = JSONObject()
//    var imageBitmap: Bitmap? = null
//    var qrBitmap: Bitmap? = null
//    private val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.US)
//
//    fun saveImage(bitmap: Bitmap, name: String): String {
//        val file = File(imageDirectory, "$name.jpg")
//        return try {
//            val outputStream = FileOutputStream(file)
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//            outputStream.flush()
//            outputStream.close()
//            file.absolutePath
//        } catch (e: Exception) {
//            e.printStackTrace()
//            ""
//        }
//    }
//
//    fun generateQR(): Bitmap? {
//        return try {
//            val multiFormatWriter = PDF417Writer()
//            val bitMatrix: BitMatrix = multiFormatWriter.encode(jsonData.toString(), BarcodeFormat.QR_CODE, 400, 400)
//            // Code to create a Bitmap from bitMatrix...
//            // return bitmap
//            null // Replace with actual Bitmap generation
//        } catch (e: WriterException) {
//            e.printStackTrace()
//            null
//        }
//    }
//
//    fun formatDate(calendar: Calendar): String {
//        return dateFormatter.format(calendar.time)
//    }
}
