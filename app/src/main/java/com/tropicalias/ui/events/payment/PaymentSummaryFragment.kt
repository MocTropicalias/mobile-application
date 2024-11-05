package com.tropicalias.ui.events.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.tropicalias.R
import com.tropicalias.databinding.FragmentPaymentSummaryBinding
import com.tropicalias.databinding.FragmentSuccessBinding
import com.tropicalias.ui.events.eventdetails.EventViewModel

class PaymentSummaryFragment : Fragment() {

    private val viewModel: EventViewModel by activityViewModels()

    private val binding: FragmentPaymentSummaryBinding get() = _binding!!
    private var _binding: FragmentPaymentSummaryBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.productListTextView.text = viewModel.event?.event?.title ?: ""
        binding.vlUnitTextView.text = "R\$ ${viewModel.event?.event?.ticketPricing}"
        binding.qtProdTextView.text = "${viewModel.buyingAmount}"
        binding.vlTotTextView.text =
            "R\$ ${viewModel.event?.event?.ticketPricing?.times(viewModel.buyingAmount)}"

        binding.voltarPaymentSummaryImageView.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, PaymentMethodFragment())
                .commitNow()
        }

        binding.button.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, QrCodeFragment())
                .commitNow()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}