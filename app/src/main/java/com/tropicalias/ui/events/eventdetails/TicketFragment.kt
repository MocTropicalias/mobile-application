package com.tropicalias.ui.events.eventdetails

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tropicalias.R
import com.tropicalias.adapter.TentAdapter
import com.tropicalias.api.model.Tent
import com.tropicalias.databinding.FragmentTicketBinding
import com.tropicalias.ui.events.payment.PaymentMethodFragment

class TicketFragment : Fragment() {

    private var _binding: FragmentTicketBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EventViewModel by activityViewModels()
    private var initialAmount = 0

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
            if (viewNotFlipped) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, EventFragment())
                    .commitNow()
            } else {
                flipView()
            }
        }

        initialAmount = viewModel.event?.amount!!
        var buyingAmount = 0


        binding.quantTicketsTextView.text = "${initialAmount}"

        binding.ComprarMenosImageButton.visibility = View.INVISIBLE
        binding.UsarTicketButton.visibility = View.INVISIBLE
        binding.precoTicketTextView.text = ""
        binding.ComprarMenosImageButton.setOnClickListener {
            Log.d("TicketFragment", "onViewCreated initital amount: ${initialAmount}")
            Log.d("TicketFragment", "onViewCreated buying amount: ${buyingAmount}")
            if (buyingAmount == 1) {
                buyingAmount = 0
                binding.ComprarMenosImageButton.visibility = View.INVISIBLE
                binding.quantTicketsTextView.text = "${initialAmount + buyingAmount}"
                binding.UsarTicketButton.visibility = View.INVISIBLE
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

        val adapter = TentAdapter(viewModel.event!!.event.barracas) { tent -> flipView(tent) }
        binding.listaProdutosRecyclerView.adapter = adapter
        binding.listaProdutosRecyclerView.layoutManager = LinearLayoutManager(requireContext())

    }

    private var viewNotFlipped = true
    private fun flipView(tent: Tent? = null) {
        val showTicketLayout =
            !viewNotFlipped // Toggle between Ticket and Tent layout based on viewFlipped state

        // Set text values for the Ticket layout
        if (tent != null) {
            binding.tentTextView.text = tent.name
            binding.amountNowTextView.text = initialAmount.toString()
            binding.amountNeededTextView.text = tent.ticketPrice.toString()
            binding.amountResultTextView.text = (initialAmount - tent.ticketPrice).toString()
            binding.concluirCompraButton2.text = "Usar Tickets"
            binding.concluirCompraButton2.backgroundTintList =
                ColorStateList.valueOf(getColor(requireContext(), R.color.azul_claro))
            if ((initialAmount - tent.ticketPrice) < 0) {
                binding.concluirCompraButton2.backgroundTintList =
                    ColorStateList.valueOf(getColor(requireContext(), R.color.vermelho))
                binding.concluirCompraButton2.text = "Tickets Insuficientes"
            }
        }

        // Animate rotation to 90 degrees (first half of flip)
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
                binding.TicketLayout.visibility = if (showTicketLayout) View.VISIBLE else View.GONE
                binding.TentLayout.visibility = if (showTicketLayout) View.GONE else View.VISIBLE

                // Animate back from -90 to 0 degrees (second half of flip)
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
        viewNotFlipped = !viewNotFlipped // Toggle the flip state
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
