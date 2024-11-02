package com.tropicalias.ui.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tropicalias.R
import com.tropicalias.adapter.PostAdapter
import com.tropicalias.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentSearchBinding? = null
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: PostAdapter

    val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val userId = data?.getLongExtra("userId", -1)
                if (userId != null) {
                    val bundle = Bundle().apply {
                        putLong("userId", userId)
                    }
                    findNavController().navigate(
                        R.id.action_navigation_home_to_navigation_profile,
                        bundle,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.navigation_home, true)
                            .build()
                    )
                }
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adapter = PostAdapter(emptyList(), findNavController(), activityResultLauncher)
        viewModel.adapter = adapter
        _binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var filterFollow = false
        var filterLike = false

        fun search() {
            binding.searchLayout.visibility = View.GONE
            binding.notFoundLayout.visibility = View.GONE
            binding.loading.visibility = View.VISIBLE
            val text = binding.searchEditText.text.toString()
            viewModel.search(text, filterFollow, filterLike)

            binding.swipeRefreshLayout.setOnRefreshListener {
                search()
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }

        binding.posts.adapter = adapter
        binding.posts.layoutManager = LinearLayoutManager(requireContext())
        if (adapter.posts.isNotEmpty()) {
            binding.searchLayout.visibility = View.GONE
        }

        binding.followingTextView.setOnClickListener {
            filterFollow = !filterFollow
            filterLike = false
            binding.likedTextView.setTextColor(getColor(requireContext(), R.color.black))
            if (filterFollow) {
                binding.followingTextView.setTextColor(getColor(requireContext(), R.color.ciano))
                search()
            } else {
                binding.followingTextView.setTextColor(getColor(requireContext(), R.color.black))
                search()
            }
        }

        binding.likedTextView.setOnClickListener {
            filterLike = !filterLike
            filterFollow = false
            binding.followingTextView.setTextColor(getColor(requireContext(), R.color.black))
            if (filterLike) {
                binding.likedTextView.setTextColor(getColor(requireContext(), R.color.ciano))
                search()
            } else {
                binding.likedTextView.setTextColor(getColor(requireContext(), R.color.black))
                search()
            }
        }


        binding.searchImageView.setOnClickListener {
            search()
        }

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                true
            } else {
                false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}