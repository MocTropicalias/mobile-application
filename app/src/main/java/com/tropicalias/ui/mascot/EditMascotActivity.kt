package com.tropicalias.ui.mascot

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.tropicalias.adapter.ColorAdapter
import com.tropicalias.api.model.Color
import com.tropicalias.databinding.ActivityEditMascotBinding

class EditMascotActivity : AppCompatActivity() {

    lateinit var binding: ActivityEditMascotBinding
    lateinit var adapter: ColorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMascotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageViewCancelEditAraci.setOnClickListener {
            onBackPressed()
        }


        adapter = ColorAdapter(
            listOf(
                Color("#37D248", "#37D248", "#37D248"),
                Color("#37D248", "#37D248", "#37D248"),
                Color("#37D248", "#37D248", "#37D248")
            ), binding
        )

        binding.recyclerViewCor.adapter = adapter
        binding.recyclerViewCor.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.recyclerViewCor)


    }
}