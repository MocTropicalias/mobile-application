package com.tropicalias.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tropicalias.R
import com.tropicalias.adapter.PostAdapter
import com.tropicalias.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentSearchBinding? = null
    private val viewModel: SearchViewModel by viewModels()
    private val adapter = PostAdapter(emptyList())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            val text = binding.searchEditText.text.toString()
            viewModel.search(text, filterFollow, filterLike)
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