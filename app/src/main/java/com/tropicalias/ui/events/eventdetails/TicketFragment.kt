package com.tropicalias.ui.events.eventdetails

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tropicalias.databinding.FragmentTicketBinding

class TicketFragment : Fragment() {

    private var _binding: FragmentTicketBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EventViewModel by viewModels()

    private lateinit var fromDatePickerDialog: DatePickerDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTicketBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        setDateTimeField()
//        binding.generateQR.setOnClickListener { generateQR() }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
