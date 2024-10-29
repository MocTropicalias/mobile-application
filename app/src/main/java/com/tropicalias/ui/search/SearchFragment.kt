package com.tropicalias.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
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

        binding.posts.adapter = adapter
        binding.posts.layoutManager = LinearLayoutManager(requireContext())

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.searchLayout.visibility = View.GONE

                val text = binding.searchEditText.text.toString()
                val filterFollow = false
                val filterLike = false

                viewModel.search(text, filterFollow, filterLike)

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