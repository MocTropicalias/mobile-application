package com.tropicalias

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tropicalias.databinding.ActivityAuthenticationBinding
import com.tropicalias.ui.auth.LoginFragment

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            // Carrega o fragmento de login inicialmente
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .commit()
        }
    }
}
