package com.tropicalias.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tropicalias.api.model.Color
import com.tropicalias.databinding.ActivityEditMascotBinding
import com.tropicalias.databinding.ItemColorBinding

class ColorAdapter(val colors: List<Color>, val activityBinding: ActivityEditMascotBinding) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ColorViewHolder(
            ItemColorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = colors.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ColorViewHolder).bind(colors[position])
    }

    inner class ColorViewHolder(val binding: ItemColorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(color: Color) {
            binding.color.setBackgroundColor(android.graphics.Color.parseColor(color.colorPrimary))


        }
    }

}