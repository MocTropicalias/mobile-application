package com.tropicalias.ui.payment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tropicalias.R

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SuccessFragment.newInstance())
                .commitNow()
        }
    }
}