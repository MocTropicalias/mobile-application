package com.tropicalias.ui.events.eventdetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.tropicalias.R
import com.tropicalias.api.model.Ticket
import com.tropicalias.databinding.FragmentEventDetailsBinding
import java.text.SimpleDateFormat
import java.util.Locale

class EventFragment() : Fragment() {

    private var _binding: FragmentEventDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EventViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fechareventoImageView.setOnClickListener {
            requireActivity().onBackPressed()
        }

        Log.d("EventFragmentgment", "onViewCreated: ${viewModel.eventId}")

        viewModel.event?.let {
            loadEvent(it)
        }

        viewModel.loadEvent(viewModel.eventId) { event ->
            loadEvent(event)
            viewModel.event = event
        }

    }

    private fun loadEvent(ticket: Ticket) {
        val event = ticket.event
        binding.fragmentEventTituloTextView.text = event.title

//        binding.descricaoEventoFullTextView.text = event.description

        binding.quantidadeTicketsTextView.text = ticket.amount.toString()

        binding.precoTicketTextView.text = "Cada ticket custa R\$${event.ticketPricing}"

        binding.localEventoFullTextView.text = event.local

        binding.dataEventoFullTextView.text =
            SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale("pt", "BR")).format(event.startDate)

        binding.buttticketEventoButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, TicketFragment())
                .commitNow()
        }

        binding.descricaoEventoFullTextView.text = event.description

        Glide.with(requireContext())
            .load(event.image)
            .into(binding.imageFullEventoImageView)

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}