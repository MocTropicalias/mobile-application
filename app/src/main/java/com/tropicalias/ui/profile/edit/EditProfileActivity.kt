package com.tropicalias.ui.profile.edit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tropicalias.R
import com.tropicalias.databinding.ActivitySettingsBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, EditProfileFragment())
                .commit()
        }
    }
}
