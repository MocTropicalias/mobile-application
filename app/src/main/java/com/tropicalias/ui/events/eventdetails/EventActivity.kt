package com.tropicalias.ui.events.eventdetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tropicalias.R

class EventActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        val eventId = intent.data?.getQueryParameter("eventId")
        val purchaseId = intent.data?.getQueryParameter("purchaseId")

        if (savedInstanceState == null) {
            if (purchaseId != null && eventId != null) {
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, SpendTicketsFragment())
//                    .commitNow()
            } else if (eventId != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, EventFragment(eventId.toLong()))
                    .commitNow()
            }
        }
    }
}