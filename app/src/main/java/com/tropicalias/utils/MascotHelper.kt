package com.tropicalias.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.view.WindowInsetsController
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.tropicalias.R
import com.tropicalias.api.model.Color
import com.tropicalias.api.model.Mascote
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.ui.mascot.EditMascotActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MascotHelper {
    companion object {
        fun changeMascotColors(
            color: Color,
            araci: ImageView,
            context: Context,
            background: Drawable,
            saveButton: Button? = null,
            activity: EditMascotActivity? = null
        ) {
            val colorPrimary = android.graphics.Color.parseColor(color.colorPrimaryHex)
            val colorSecondary = android.graphics.Color.parseColor(color.colorSecondaryHex)
            val colorBackground = android.graphics.Color.parseColor(color.colorBackgroundHex)
            var colorThird: Int? = null
            if (color.id == (0).toLong()) {
                colorThird = android.graphics.Color.parseColor("#257520")
            }

            val drawablePartBody =
                ContextCompat.getDrawable(context, R.drawable.araci_body)?.mutate()
            val drawablePartGreen =
                ContextCompat.getDrawable(context, R.drawable.araci_green)?.mutate()
            val drawablePartSecondary =
                ContextCompat.getDrawable(context, R.drawable.araci_secondary)?.mutate()
            val drawablePartPrimary =
                ContextCompat.getDrawable(context, R.drawable.araci_primary)?.mutate()

            drawablePartSecondary?.setTint(colorSecondary)
            drawablePartGreen?.setTint(colorThird ?: colorPrimary)
            drawablePartPrimary?.setTint(colorPrimary)
            saveButton?.backgroundTintList = ColorStateList.valueOf(colorPrimary)

            background.setTint(colorBackground)


            if (activity != null) {
                activity.window.statusBarColor = colorBackground
                val windowInsetsController = activity.window?.decorView?.windowInsetsController
                windowInsetsController?.setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            }

            araci.setImageDrawable(
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

        fun getMascot(id: Long, callback: (mascot: Mascote) -> Unit) {
            ApiRepository.getInstance().getSQL().getMascot(id).enqueue(object :
                Callback<Mascote> {
                override fun onResponse(req: Call<Mascote>, res: Response<Mascote>) {
                    res.body()?.let {
                        ApiRepository.getInstance().mascot.value = it
                        callback(it)
                    }
                }

                override fun onFailure(req: Call<Mascote>, e: Throwable) {
                    GlobalScope.launch {
                        delay(10000)
                        getMascot(id, callback)
                    }
                }

            })
        }

    }
}