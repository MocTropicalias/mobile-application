package com.tropicalias.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tropicalias.MainActivity
import com.tropicalias.adapter.ProfileAdapter
import com.tropicalias.databinding.FragmentProfilePictureBinding
import com.wajahatkarim3.easyflipviewpager.CardFlipPageTransformer2


class ProfilePictureFragment : Fragment() {

    private var _binding: FragmentProfilePictureBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthenticationViewModel by activityViewModels()
    private lateinit var adapter: ProfileAdapter
    private var imagePicked = false
    private val TAG = "ProfilePictureFragment"

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val imageUri = data?.data
                val finalUri = imageUri ?: adapter.imageUrl
                finalUri?.let {
                    Toast.makeText(requireContext(), "Salvando imagem", Toast.LENGTH_SHORT).show()
                    viewModel.uploadImageToFirebase(finalUri)
                }
                imagePicked = true
                imagePicked = true
                binding.continueButton.text = "Continuar"
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilePictureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Get ViewPager2 and Set Adapter
        adapter = ProfileAdapter(pickImageLauncher)
        binding.viewPager.setAdapter(adapter)
        binding.viewPager.setCurrentItem(Int.MAX_VALUE / 2, false)

        // Create an object of page transformer
        val cardFlipPageTransformer = CardFlipPageTransformer2()
        cardFlipPageTransformer.isScalable = false

        // Assign the page transformer to the ViewPager2.
        binding.viewPager.setPageTransformer(cardFlipPageTransformer)

        binding.continueButton.setOnClickListener {
            if (imagePicked) {
                viewModel.storeImageUrl()
            }
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        }

        viewModel.imageUrl.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.imageUrl = it
                adapter.notifyDataSetChanged()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
