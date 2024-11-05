package com.tropicalias.ui.profile.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tropicalias.R
import com.tropicalias.databinding.FragmentRestrictedAreaBinding
import com.tropicalias.databinding.FragmentSecurityBinding

class RestrictedAreaFragment : Fragment() {

    private var _binding: FragmentRestrictedAreaBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SecurityViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestrictedAreaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewCloseRestricted.setOnClickListener {
            requireActivity().onBackPressed()
        }


        binding.webViewRestricted.loadUrl("")


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}