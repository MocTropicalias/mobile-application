package com.tropicalias.ui.home.newpost

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tropicalias.R

class NewPostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, NewPostFragment())
                .commitNow()
        }
    }
}