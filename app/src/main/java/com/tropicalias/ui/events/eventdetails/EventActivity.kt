package com.tropicalias.ui.events.eventdetails

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.tropicalias.R

class EventActivity : AppCompatActivity() {

    private val viewModel: EventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        viewModel.eventId = intent.data?.getQueryParameter("eventId")?.toLong() ?: -1
        viewModel.purchaseId = intent.data?.getQueryParameter("purchaseId")

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