package com.tropicalias.utils

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.tropicalias.R

class DrawableHandler {
    companion object {
        private fun shakeAnimation(view: View) {
            val animator = ObjectAnimator.ofFloat(
                view,
                "translationX",
                *floatArrayOf(0F, 25F, -25F, 25F, -25F, 15F, -15F, 6F, -6F, 0F)
            );
            animator.setDuration(500); // Duração da animação
            animator.start(); // Inicia a animação
        }

        fun setInvalidDrawable(editText: EditText, context: Context) {
            (editText.background as GradientDrawable).setStroke(
                dpToPx(3, context),
                ContextCompat.getColor(context, R.color.vermelho)
            )
            editText.compoundDrawables[0]?.mutate()?.setTint(
                ContextCompat.getColor(context, R.color.vermelho)
            )
            shakeAnimation(editText)
        }

        fun setValidDrawable(editText: EditText, context: Context) {
            (editText.background as GradientDrawable).setStroke(
                dpToPx(3, context),
                ContextCompat.getColor(context, R.color.black)
            )
            editText.compoundDrawables[0]?.mutate()?.setTint(
                ContextCompat.getColor(context, R.color.black)
            )
        }

        private fun dpToPx(dp: Int, context: Context): Int {
            return (dp * context.resources.displayMetrics.density).toInt()
        }
    }
}