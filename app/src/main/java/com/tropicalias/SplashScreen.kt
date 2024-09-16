package com.tropicalias

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val isLoggedIn = getLogin()

        GlobalScope.launch(Dispatchers.Main) {
            delay(2000)
            intent = if (isLoggedIn) {
                Intent(this@SplashScreen, MainActivity::class.java)
            } else {
                Intent(this@SplashScreen, AuthenticationActivity::class.java)
            }
            startActivity(intent)
            finish()
        }
    }

    private fun getLogin(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

}