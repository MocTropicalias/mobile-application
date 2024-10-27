package com.tropicalias.ui.profile.settings;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tropicalias.databinding.FragmentUserTermsBinding

class UserTermsFragment : Fragment() {

    private var _binding: FragmentUserTermsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserTermsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewCloseUserTerms.setOnClickListener {
            requireActivity().onBackPressed()
        }


        binding.webViewUserTerms.loadUrl("https://landing-page-lw54.onrender.com/Termos.html")


    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}