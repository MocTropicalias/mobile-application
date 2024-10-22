package com.tropicalias.adapter

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tropicalias.api.model.Event
import com.tropicalias.databinding.ItemEventBinding

class EventsAdapter(var posts: List<Event>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PostViewHolder(
            ItemEventBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = posts.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PostViewHolder).bind(posts[position])
    }

    inner class PostViewHolder(val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.eventoTituloTextView.text = event.title
            binding.dataEventoTextView.text = DateFormat.format("dd/MM/yyyy", event.date)
            binding.enderecoEventoTextView.text = event.local
            Glide.with(binding.root.context)
                .load(event.eventImage)
                .into(binding.eventoImageView)
        }
    }

}