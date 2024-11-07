package com.tropicalias.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tropicalias.api.model.Tent
import com.tropicalias.databinding.ItemTentBinding


//import com.google.zxing.BarcodeFormat
//import com.google.zxing.WriterException
//import com.google.zxing.common.BitMatrix
//import com.google.zxing.pdf417.PDF417Writer

class TentAdapter(val tents: List<Tent>, val flipView: (Tent) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TentViewHolder(
            ItemTentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = tents.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TentViewHolder).bind(tents[position])
    }

    inner class TentViewHolder(val binding: ItemTentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tent: Tent) {
            binding.tentNameTextView.text = tent.name
            binding.ticketCostTextView.text = tent.ticketPrice.toString()
            binding.buyButton.setOnClickListener { v ->
//                (v.context as FragmentActivity).supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, TentDetailsFragment())
//                    .commit()
                flipView(tent)
            }
        }
    }

}

//    private fun setDateTimeField() {
//        val calendar = Calendar.getInstance()
//        fromDatePickerDialog = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
//            val newDate = Calendar.getInstance().apply {
//                set(year, month, dayOfMonth)
//            }
//            binding.dob.setText(viewModel.formatDate(newDate))
//        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
//    }
//
//    private fun generateQR() {
//        // Validate input and set jsonData in ViewModel
//        // Generate QR and update UI
//        viewModel.qrBitmap = viewModel.generateQR()
//        binding.qrImage.setImageBitmap(viewModel.qrBitmap)
//    }

//        setDateTimeField()
//        binding.generateQR.setOnClickListener { generateQR() }


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