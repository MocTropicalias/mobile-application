package com.tropicalias

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.tropicalias.utils.ApiHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val isLoggedIn = getLogin()

        if (isLoggedIn) {
            ApiHelper.getUser()
        }

        GlobalScope.launch(Dispatchers.Main) {
            delay(1500)
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