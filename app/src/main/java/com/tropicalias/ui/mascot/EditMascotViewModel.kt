package com.tropicalias.ui.mascot

import androidx.lifecycle.ViewModel
import com.tropicalias.api.model.Color
import com.tropicalias.api.repository.ApiRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditMascotViewModel : ViewModel() {

    fun getColors(callback: (List<Color>) -> Unit) {
        ApiRepository.getInstance().getSQL().getAllColors().enqueue(object : Callback<List<Color>> {
            override fun onResponse(req: Call<List<Color>>, res: Response<List<Color>>) {
                res.body()?.let { callback(it) }
            }

            override fun onFailure(req: Call<List<Color>>, e: Throwable) {
                GlobalScope.launch {
                    delay(10000)
                    getColors(callback)
                }
            }

        })
    }

}
