package com.tropicalias.ui.mascot

import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import android.view.WindowInsetsController
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tropicalias.R
import com.tropicalias.adapter.ColorAdapter
import com.tropicalias.api.model.Color
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.databinding.ActivityEditMascotBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        changeMascotColors(Color(0, "006996", "E45F15", "32B6F4"))
    }

    fun changeMascotColors(color: Color) {
        val colorPrimary = android.graphics.Color.parseColor(color.colorPrimaryHex)
        val colorSecondary = android.graphics.Color.parseColor(color.colorSecondaryHex)
        val colorBackground = android.graphics.Color.parseColor(color.colorBackgroundHex)
        var colorThird: Int? = null
        if (color.id == (0).toLong()) {
            colorThird = android.graphics.Color.parseColor("#257520")
        }

        val drawablePartBody =
            ContextCompat.getDrawable(binding.root.context, R.drawable.araci_body)?.mutate()
        val drawablePartGreen =
            ContextCompat.getDrawable(binding.root.context, R.drawable.araci_green)?.mutate()
        val drawablePartSecondary =
            ContextCompat.getDrawable(binding.root.context, R.drawable.araci_secondary)?.mutate()
        val drawablePartPrimary =
            ContextCompat.getDrawable(binding.root.context, R.drawable.araci_primary)?.mutate()

        drawablePartSecondary?.setTint(colorSecondary)
        drawablePartGreen?.setTint(colorThird ?: colorPrimary)
        drawablePartPrimary?.setTint(colorPrimary)

        binding.bgEditMascot.drawable.setTint(colorBackground)
        this.window.statusBarColor = colorBackground
        val windowInsetsController = this.window?.decorView?.windowInsetsController
        windowInsetsController?.setSystemBarsAppearance(
            0,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )


        binding.imgAraciEdit.setImageDrawable(
            LayerDrawable(
                arrayOf(
                    drawablePartSecondary,
                    drawablePartPrimary,
                    drawablePartGreen,
                    drawablePartBody
                )
            )
        )
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

                    changeMascotColors(color)
                }
            }

        }
    }

}