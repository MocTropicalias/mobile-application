package com.tropicalias.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tropicalias.MainViewModel
import com.tropicalias.R
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
        val adapter = PostAdapter(emptyList(), findNavController(), activityResultLauncher)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val navigateToProfile = data?.getBooleanExtra("navigateToProfile", false) ?: false
            val userId = data?.getLongExtra("userId", -1)
            val postId = data?.getStringExtra("postId")

            if (navigateToProfile && userId != null) {
                findNavController().navigate(
                    R.id.action_navigation_home_to_navigation_profile,
                    bundleOf(Pair("userId", userId), Pair("postId", postId)),
                    NavOptions.Builder()
                        .setPopUpTo(R.id.navigation_home, true)
                        .build()
                )
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}