package com.tropicalias.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.tropicalias.MainViewModel
import com.tropicalias.adapter.PostAdapter
import com.tropicalias.api.model.Post
import com.tropicalias.databinding.FragmentHomeBinding
import com.tropicalias.ui.profile.ProfileViewModel
import java.util.Date

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val viewModel: ProfileViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private val postList: List<Post> = listOf(
        Post(
            1,
            11,
            "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
            "tropicalias",
            "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
            "conteudo",
            12,
            emptyList(),
            Date()
        ),

        Post(
            2,
            11,
            "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
            "tropicalias",
            "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
            "conteudo",
            12,
            emptyList(),
            Date()
        ),

        Post(
            3,
            11,
            "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
            "tropicalias",
            "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
            "conteudo",
            12,
            emptyList(),
            Date()
        ),


        )



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
        Log.d("HOME", "onViewCreated: Home")
        val adapter = PostAdapter(postList)
        binding.posts.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        binding.posts.adapter = adapter

        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(requireContext(), NewPostActivity::class.java))
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}