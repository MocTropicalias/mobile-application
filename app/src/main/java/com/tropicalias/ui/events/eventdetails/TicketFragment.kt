package com.tropicalias.ui.events.eventdetails

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tropicalias.R
import com.tropicalias.adapter.TentAdapter
import com.tropicalias.databinding.FragmentTicketBinding
import com.tropicalias.ui.events.payment.PaymentMethodFragment
import com.tropicalias.ui.profile.settings.SettingsActivity
import kotlin.time.times

class TicketFragment : Fragment() {

    private var _binding: FragmentTicketBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EventViewModel by activityViewModels()

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


        binding.botaoFecharTicketImageButton.setOnClickListener {
            flipView()
//            requireActivity().onBackPressed()
        }

        Log.d("TicketFragment", "onViewCreated: ${viewModel.event}")
        val initialAmount = viewModel.event?.amount!!
        var buyingAmount = 0

        binding.quantTicketsTextView.text = "${initialAmount}"

        binding.ComprarMenosImageButton.visibility = View.INVISIBLE
        binding.UsarTicketButton.visibility = View.GONE
        binding.precoTicketTextView.text = ""
        binding.ComprarMenosImageButton.setOnClickListener {
            Log.d("TicketFragment", "onViewCreated initital amount: ${initialAmount}")
            Log.d("TicketFragment", "onViewCreated buying amount: ${buyingAmount}")
            if (buyingAmount == 1) {
                buyingAmount = 0
                binding.ComprarMenosImageButton.visibility = View.INVISIBLE
                binding.quantTicketsTextView.text = "${initialAmount + buyingAmount}"
                binding.UsarTicketButton.visibility = View.GONE
                binding.precoTicketTextView.text = ""
            } else {
                buyingAmount -= 1
                binding.quantTicketsTextView.text = buyingAmount.toString()
                binding.precoTicketTextView.text =
                    "Preço: R\$${(buyingAmount * viewModel.event!!.event.ticketPricing)}"
                binding.UsarTicketButton.text = "Comprar ${buyingAmount} Tickets"
            }
        }

        binding.ComprarTicketImageButton.setOnClickListener {
            binding.ComprarMenosImageButton.visibility = View.VISIBLE
            buyingAmount += 1
            binding.quantTicketsTextView.text = "${initialAmount + buyingAmount}"
            binding.UsarTicketButton.visibility = View.VISIBLE
            binding.precoTicketTextView.text =
                "Preço: R\$${(buyingAmount * viewModel.event!!.event.ticketPricing)}"
            binding.UsarTicketButton.text = "Comprar ${buyingAmount} Tickets"
        }

        binding.UsarTicketButton.setOnClickListener {
            viewModel.buyingAmount = buyingAmount
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, PaymentMethodFragment())
                .commitNow()
        }

        val adapter = TentAdapter(viewModel.event!!.event.barracas)
        binding.listaProdutosRecyclerView.adapter = adapter
        binding.listaProdutosRecyclerView.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun flipView() {
        val showTicketLayout = binding.TicketLayout?.visibility == View.GONE

        // Animate rotation to 90 degrees
        val animator = ObjectAnimator.ofFloat(
            if (showTicketLayout) binding.TentLayout else binding.TicketLayout,
            "rotationY",
            0f,
            90f
        )
        animator.duration = 300

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)

                // Toggle visibility of the layouts at halfway point
                binding.TicketLayout?.visibility = if (showTicketLayout) View.VISIBLE else View.GONE
                binding.TentLayout?.visibility = if (showTicketLayout) View.GONE else View.VISIBLE

                // Animate back from -90 to 0 degrees
                val reverseAnimator = ObjectAnimator.ofFloat(
                    if (showTicketLayout) binding.TicketLayout else binding.TentLayout,
                    "rotationY",
                    -90f,
                    0f
                )
                reverseAnimator.duration = 300
                reverseAnimator.start()
            }
        })

        animator.start()
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
