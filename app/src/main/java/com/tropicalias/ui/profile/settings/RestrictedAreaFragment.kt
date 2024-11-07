package com.tropicalias.ui.profile.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tropicalias.databinding.FragmentRestrictedAreaBinding

class RestrictedAreaFragment : Fragment() {

    private var _binding: FragmentRestrictedAreaBinding? = null
    private val binding get() = _binding!!

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


        binding.webViewRestricted.settings.javaScriptEnabled = true
        binding.webViewRestricted.loadUrl("https://app.powerbi.com/view?r=eyJrIjoiNTFlMWM2MTYtZjc1NC00NmVmLTk1OTktM2ZiYTUyNDI3ZWI3IiwidCI6ImIxNDhmMTRjLTIzOTctNDAyYy1hYjZhLTFiNDcxMTE3N2FjMCJ9")


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}