package com.tropicalias.ui.events

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
import java.util.Date

class EventsFragment : Fragment() {

    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EventsViewModel by viewModels()

    private val events: MutableList<Event> = mutableListOf(
        Event(
            id = 0,
            title = "Tropicalias",
            eventImage = Uri.EMPTY,
            date = Date().time,
            local = "aqui"
        ),
        Event(
            id = 1,
            title = "TechExpo",
            eventImage = Uri.EMPTY,
            date = Date().time,
            local = "aqui"
        ),
        Event(
            id = 2,
            title = "Teste do Albano",
            eventImage = "https://i.pinimg.com/736x/02/4b/7b/024b7b7eee0bb8e98528c6a872e1f761.jpg".toUri(),
            date = Date().time,
            local = "Sonhos do Albano"
        )
    )

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

        val adapter = EventsAdapter(events)
        binding.recyclerView.adapter = adapter

        binding.recyclerView.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(requireContext())


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}