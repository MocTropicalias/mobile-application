package com.tropicalias.adapter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.tropicalias.api.model.Ticket
import com.tropicalias.databinding.ItemEventBinding
import com.tropicalias.ui.events.eventdetails.EventActivity
import com.tropicalias.utils.ApiHelper


class EventsAdapter(var eventTickets: List<Ticket>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return EventViewHolder(
            ItemEventBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = eventTickets.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as EventViewHolder).bind(eventTickets[position])
    }

    inner class EventViewHolder(val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(eventTicket: Ticket) {
            val event = eventTicket.event

            binding.eventoTituloTextView.text = event.title
            binding.dataEventoTextView.text = DateFormat.format("dd/MM/yyyy", event.startDate)
            binding.enderecoEventoTextView.text = event.local
            Glide.with(binding.root.context)
                .load(event.image)
                .into(binding.eventoImageView)
            binding.root.setOnClickListener {
                if (FirebaseAuth.getInstance().currentUser == null) {
                    Toast.makeText(
                        binding.root.context,
                        "Entre na sua conta para ver seus Tickets!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                val intent = Intent(binding.root.context, EventActivity::class.java)
                val uri = Uri.parse("tropicalias://event/?eventId=${event.id}")

                val args = Bundle()
                val personJsonString: String = ApiHelper.getGsonParser().toJson(eventTicket)
                args.putString("eventTicket", personJsonString)

                intent.data = uri
                startActivity(binding.root.context, intent, args)
            }
        }
    }

}