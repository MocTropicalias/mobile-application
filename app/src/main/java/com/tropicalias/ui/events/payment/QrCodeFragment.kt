package com.tropicalias.ui.events.payment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tropicalias.R
import com.tropicalias.databinding.FragmentQrCodeBinding
import com.tropicalias.databinding.FragmentSuccessBinding
import com.tropicalias.ui.events.eventdetails.EventViewModel

class QrCodeFragment : Fragment() {

    private val viewModel: EventViewModel by activityViewModels()

    private val binding: FragmentQrCodeBinding get() = _binding!!
    private var _binding: FragmentQrCodeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQrCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.copiarPixTextView.setOnClickListener {
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            // Create a new ClipData with the text
            val clip = ClipData.newPlainText("Copied Text", "Código do PIX")

            // Set the ClipData to the clipboard
            clipboard.setPrimaryClip(clip)
        }

        binding.sairQrCodeImageView.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.concluirCompraButton.setOnClickListener {
            viewModel.addTickets {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, SuccessFragment())
                    .commitNow()
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}