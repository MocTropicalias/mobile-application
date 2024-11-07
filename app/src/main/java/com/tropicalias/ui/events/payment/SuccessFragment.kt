package com.tropicalias.ui.events.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tropicalias.databinding.FragmentSuccessBinding
import com.tropicalias.ui.events.eventdetails.EventViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SuccessFragment : Fragment() {

    private val viewModel: EventViewModel by activityViewModels()

    private val binding: FragmentSuccessBinding get() = _binding!!
    private var _binding: FragmentSuccessBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sairSucessoImageView.setOnClickListener {
            requireActivity().onBackPressed()
        }

        GlobalScope.launch {
            delay(5000)
            activity?.onBackPressed()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


}