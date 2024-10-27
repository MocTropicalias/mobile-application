package com.tropicalias.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tropicalias.MainViewModel
import com.tropicalias.adapter.PostAdapter
import com.tropicalias.databinding.FragmentHomeBinding
import com.tropicalias.ui.posts.newpost.NewPostActivity
import com.tropicalias.ui.profile.ProfileViewModel
import com.tropicalias.utils.ApiHelper

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
        val layoutManager = LinearLayoutManager(requireContext())
        binding.posts.layoutManager = layoutManager
        val adapter = PostAdapter(emptyList())
        binding.loading.visibility = View.VISIBLE
        ApiHelper.getPosts {
            if (_binding != null) {
                adapter.posts = it
                binding.posts.adapter = adapter
                adapter.notifyDataSetChanged()
                binding.loading.visibility = View.GONE
            }
        }

        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(requireContext(), NewPostActivity::class.java))
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            ApiHelper.getPosts { posts ->
                if (_binding != null) {
                    adapter.posts = posts
                    adapter.notifyDataSetChanged()
                }
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}