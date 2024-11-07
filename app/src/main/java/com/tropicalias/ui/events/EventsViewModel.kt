package com.tropicalias.ui.events

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tropicalias.api.model.Event
import com.tropicalias.api.model.Ticket
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.utils.ApiHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

class EventsViewModel : ViewModel() {

    private val events: MutableList<Ticket> = mutableListOf(

    )

    fun getEvents(callback: (List<Ticket>) -> Unit) {
        ApiHelper.getUser { user ->
            ApiRepository.getInstance().getSQL().getUserRegisteredEvents(user.id!!)
                .enqueue(object : Callback<List<Ticket>> {
                    override fun onResponse(req: Call<List<Ticket>>, res: Response<List<Ticket>>) {
                        res.body()?.let { callback(it) }
                    }

                    override fun onFailure(req: Call<List<Ticket>>, e: Throwable) {
                        GlobalScope.launch {
                            delay(10000)
                            getEvents(callback)
                        }
                    }

                })

            callback(events)
        }
    }

}