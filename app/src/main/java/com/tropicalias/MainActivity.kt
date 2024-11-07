package com.tropicalias

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.databinding.ActivityMainBinding
import com.tropicalias.utils.RequestPermission
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main)
        NavigationUI.setupWithNavController(binding.navView, navController)

        RequestPermission.requestNotificationPermission(this, this)

        ApiRepository.getInstance().getRedis().incr().enqueue(object : Callback<Unit> {
            override fun onResponse(p0: Call<Unit>, p1: Response<Unit>) {

            }

            override fun onFailure(p0: Call<Unit>, p1: Throwable) {

            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        ApiRepository.getInstance().getRedis().decr().enqueue(object : Callback<Unit> {
            override fun onResponse(p0: Call<Unit>, p1: Response<Unit>) {

            }

            override fun onFailure(p0: Call<Unit>, p1: Throwable) {

            }
        })
    }

}