package com.tropicalias.ui.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

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