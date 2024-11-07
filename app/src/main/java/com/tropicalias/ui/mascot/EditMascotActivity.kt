package com.tropicalias.ui.mascot

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tropicalias.adapter.ColorAdapter
import com.tropicalias.api.model.Color
import com.tropicalias.databinding.ActivityEditMascotBinding
import com.tropicalias.utils.ApiHelper
import com.tropicalias.utils.MascotHelper

class EditMascotActivity : AppCompatActivity() {

    lateinit var binding: ActivityEditMascotBinding
    val adapter = ColorAdapter(emptyList())
    lateinit var snapHelper: LinearSnapHelper
    lateinit var layoutManager: LinearLayoutManager
    val viewModel: EditMascotViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMascotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageViewCancelEditAraci.setOnClickListener {
            onBackPressed()
        }

        viewModel.getColors {
            adapter.colors = it
            adapter.notifyDataSetChanged()
        }

        binding.recyclerViewCor.adapter = adapter
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewCor.layoutManager = layoutManager


        snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.recyclerViewCor)
        binding.recyclerViewCor.addOnScrollListener(CustomScrollListener())

        MascotHelper.changeMascotColors(
            Color(),
            binding.imgAraciEdit, this, binding.bgEditMascot.drawable,
            binding.btSaveAraci, this@EditMascotActivity
        )
        ApiHelper.getUser { user ->
            MascotHelper.getMascot(user.id!!) { mascot ->
                viewModel.mascote = mascot
                binding.editTextAraciName.setText(mascot.name)

                layoutManager.scrollToPosition(adapter.colors.indexOf(mascot.colorScheme))

                MascotHelper.changeMascotColors(
                    mascot.colorScheme,
                    binding.imgAraciEdit, this, binding.bgEditMascot.drawable,
                    binding.btSaveAraci, this@EditMascotActivity
                )
                binding.btSaveAraci.setOnClickListener {
                    val name = binding.editTextAraciName.text.toString()
                    viewModel.saveColor(name, viewModel.color) {
                        onBackPressed()
                    }
                }
            }
        }


    }

    inner class CustomScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            Log.d("ColorAdapter", "onScrollStateChanged: $newState")
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                val view = snapHelper.findSnapView(layoutManager);
                Log.d("ColorAdapter", "onScrollStateChanged: $view")
                if (view != null) {
                    val position = recyclerView.getChildAdapterPosition(view)
                    val color = adapter.colors[position]
                    viewModel.color = color
                    MascotHelper.changeMascotColors(
                        color,
                        binding.imgAraciEdit, this@EditMascotActivity,
                        binding.bgEditMascot.drawable, binding.btSaveAraci, this@EditMascotActivity
                    )
                }
            }

        }
    }

}