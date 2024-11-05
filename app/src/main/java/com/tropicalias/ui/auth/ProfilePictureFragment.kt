package com.tropicalias.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tropicalias.MainActivity
import com.tropicalias.adapter.ProfileAdapter
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.databinding.FragmentProfilePictureBinding
import com.tropicalias.utils.ApiHelper
import com.tropicalias.utils.MascotHelper
import com.wajahatkarim3.easyflipviewpager.CardFlipPageTransformer2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ProfilePictureFragment : Fragment() {

    private var _binding: FragmentProfilePictureBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthenticationViewModel by activityViewModels()
    private lateinit var adapter: ProfileAdapter
    private var imagePicked = false
    lateinit var TAG: String

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val imageUri = data?.data
                val finalUri = imageUri ?: adapter.imageUri
                finalUri?.let {
//                    Toast.makeText(requireContext(), "Salvando imagem", Toast.LENGTH_SHORT).show()
                    adapter.imageUri = finalUri
                    viewModel.imageUrl = finalUri
                    adapter.notifyDataSetChanged()
                }
                imagePicked = true
                binding.continueButton.text = "Continuar"
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilePictureBinding.inflate(inflater, container, false)
        TAG = viewModel.TAG
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get ViewPager2 and Set Adapter
        adapter = ProfileAdapter(pickImageLauncher, requireContext())
        binding.viewPager.setAdapter(adapter)
        binding.viewPager.setCurrentItem(Int.MAX_VALUE / 2, false)
        GlobalScope.launch(Dispatchers.Main) {
            delay(500)
            binding.viewPager.setCurrentItem(Int.MAX_VALUE / 2 + 1, true)
            delay(500)
            binding.viewPager.setCurrentItem(Int.MAX_VALUE / 2 + 1, true)
        }
        // Create an object of page transformer
        val cardFlipPageTransformer = CardFlipPageTransformer2()
        cardFlipPageTransformer.isScalable = true
        // Assign the page transformer to the ViewPager2.
        binding.viewPager.setPageTransformer(cardFlipPageTransformer)

        ApiHelper.getUser { user ->
            MascotHelper.getMascot(user.id!!) {
                adapter.color = it.colorScheme
                adapter.notifyDataSetChanged()
            }
        }

        ApiRepository.getInstance().mascot.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.color = it.colorScheme
                adapter.notifyDataSetChanged()
            }
        }


        binding.continueButton.setOnClickListener {
            if (imagePicked) {
                viewModel.uploadImageToFirebase()
            }
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
