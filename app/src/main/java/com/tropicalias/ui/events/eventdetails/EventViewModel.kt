package com.tropicalias.ui.events.eventdetails



import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.tropicalias.api.model.Ticket
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.utils.ApiHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventViewModel(application: Application) : AndroidViewModel(application) {

    var buyingAmount: Int = 0
    var eventId: Long = -1
    var purchaseId: String? = null
    var ticket: Ticket? = null

    fun loadEvent(eventId: Long, callback: (ticket: Ticket) -> Unit) {
        ApiHelper.getUser { user ->
            ApiRepository.getInstance().getSQL().createTicket(eventId, user.id!!)
                .enqueue(object : Callback<Ticket> {
                    override fun onResponse(req: Call<Ticket>, res: Response<Ticket>) {
                        if (res.body() != null) {
                            ticket = res.body()!!
                            callback(res.body()!!)
                        }
                    }

                    override fun onFailure(req: Call<Ticket>, e: Throwable) {
                        GlobalScope.launch {
                            delay(20000)
                            loadEvent(eventId, callback)
                        }
                    }

                })
        }
    }

    fun addTickets(callback: () -> Unit) {
        ApiHelper.getUser { user ->
            ApiRepository.getInstance().getSQL().addTickets(ticket!!.id, buyingAmount)
                .enqueue(object : Callback<Ticket> {
                    override fun onResponse(req: Call<Ticket>, res: Response<Ticket>) {
                        if (res.body() != null) {
                            ticket = res.body()!!
                            callback()
                        }
                    }

                    override fun onFailure(req: Call<Ticket>, e: Throwable) {
                        GlobalScope.launch {
                            delay(20000)
                            addTickets(callback)
                        }
                    }

                })
        }
    }

    fun spendTickets(ticketPrice: Int, callback: () -> Unit) {
        ApiHelper.getUser { user ->
            ApiRepository.getInstance().getSQL().addTickets(ticket!!.id, -ticketPrice)
                .enqueue(object : Callback<Ticket> {
                    override fun onResponse(req: Call<Ticket>, res: Response<Ticket>) {
                        if (res.body() != null) {
                            ticket = res.body()!!
                            callback()
                        }
                    }

                    override fun onFailure(req: Call<Ticket>, e: Throwable) {
                        GlobalScope.launch {
                            delay(20000)
                            addTickets(callback)
                        }
                    }

                })
        }
    }

}
