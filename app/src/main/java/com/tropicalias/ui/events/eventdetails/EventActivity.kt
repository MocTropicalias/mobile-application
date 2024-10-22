package com.tropicalias.ui.events.eventdetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tropicalias.R

class EventActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, EventFragment())
                .commitNow()
        }
    }
}