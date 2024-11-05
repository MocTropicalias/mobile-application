package com.tropicalias.ui.events

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tropicalias.adapter.EventsAdapter
import com.tropicalias.api.model.Event
import com.tropicalias.databinding.FragmentEventsBinding
import com.tropicalias.ui.events.qrcode.QrCodeActivity
import com.tropicalias.utils.ApiHelper
import java.util.Date

class EventsFragment : Fragment() {

    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EventsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = EventsAdapter(emptyList())
        binding.recyclerView.adapter = adapter

        binding.recyclerView.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(requireContext())

        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(context, QrCodeActivity::class.java))
        }

        fun loadEvents() {
            binding.loading.visibility = View.VISIBLE
            binding.searchLayout.visibility = View.GONE

            viewModel.getEvents { eventTickets ->
                if (_binding != null) {
                    adapter.eventTickets = eventTickets
                    adapter.notifyDataSetChanged()
                    binding.loading.visibility = View.GONE
                    if (adapter.eventTickets.isEmpty()) {
                        binding.searchLayout.visibility = View.VISIBLE
                    } else {
                        binding.searchLayout.visibility = View.GONE
                    }
                }

            }
        }

        loadEvents()
        binding.swipeRefreshLayout.setOnRefreshListener {
            loadEvents()
            binding.swipeRefreshLayout.isRefreshing = false
        }



    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}