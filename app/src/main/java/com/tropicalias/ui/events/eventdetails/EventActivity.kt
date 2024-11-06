package com.tropicalias.ui.events.eventdetails

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tropicalias.R
import com.tropicalias.api.model.Ticket
import com.tropicalias.utils.ApiHelper

class EventActivity : AppCompatActivity() {

    private val viewModel: EventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        viewModel.eventId = intent.data?.getQueryParameter("eventId")?.toLong() ?: -1
        viewModel.purchaseId = intent.data?.getQueryParameter("purchaseId")
        Log.d("EVENTACTIVIT", "onCreate: ${intent.extras?.getString("eventTicket")}")
        viewModel.ticket = ApiHelper.getGsonParser()
            .fromJson(intent.extras?.getString("eventTicket"), Ticket::class.java)

        if (savedInstanceState == null) {
            if (viewModel.purchaseId != null && viewModel.eventId != null) {
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, SpendTicketsFragment())
//                    .commitNow()
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, EventFragment())
                    .commitNow()
            }
        }
    }
}