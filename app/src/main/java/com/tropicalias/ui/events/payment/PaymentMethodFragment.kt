package com.tropicalias.ui.events.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tropicalias.R
import com.tropicalias.databinding.FragmentPaymentMethodBinding
import com.tropicalias.ui.events.eventdetails.EventViewModel
import com.tropicalias.ui.events.eventdetails.TicketFragment

class PaymentMethodFragment : Fragment() {

    private val viewModel: EventViewModel by activityViewModels()

    private val binding: FragmentPaymentMethodBinding get() = _binding!!
    private var _binding: FragmentPaymentMethodBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentMethodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.voltarPaymentMethodImageView.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, TicketFragment())
                .commitNow()
        }

        binding.pagamentoPixButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, PaymentSummaryFragment())
                .commitNow()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}