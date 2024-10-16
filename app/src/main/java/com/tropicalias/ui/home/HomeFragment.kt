package com.tropicalias.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.tropicalias.MainViewModel
import com.tropicalias.adapter.PostAdapter
import com.tropicalias.adapter.ProfileAdapter
import com.tropicalias.databinding.FragmentHomeBinding
import com.tropicalias.databinding.FragmentProfileBinding
import com.tropicalias.ui.profile.ProfileViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val viewModel: ProfileViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = PostAdapter(emptyList())
        binding.posts.adapter = adapter
        adapter.posts

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}