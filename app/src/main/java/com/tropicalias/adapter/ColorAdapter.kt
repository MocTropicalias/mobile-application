package com.tropicalias.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tropicalias.R
import com.tropicalias.api.model.Color
import com.tropicalias.databinding.ActivityEditMascotBinding
import com.tropicalias.databinding.ItemColorBinding
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable


class ColorAdapter(var colors: List<Color>) :
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
            binding.color.setBackgroundColor(android.graphics.Color.parseColor(color.colorPrimaryHex))
        }
    }

}