package com.tropicalias.ui.profile.edit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.tropicalias.adapter.ProfileAdapter
import com.tropicalias.databinding.FragmentEditProfileBinding
import com.tropicalias.utils.ApiHelper
import com.tropicalias.utils.InputCheck
import com.wajahatkarim3.easyflipviewpager.CardFlipPageTransformer2

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProfileAdapter
    private val viewModel: EditProfileViewModel by viewModels()
    lateinit var TAG: String

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val imageUri = data?.data
                val finalUri = imageUri ?: adapter.imageUri
                finalUri?.let {
//                    Toast.makeText(requireContext(), "Salvando imagem", Toast.LENGTH_SHORT).show()
//                    viewModel.uploadImageToFirebase(finalUri)
                    adapter.imageUri = finalUri
                    adapter.notifyDataSetChanged()
                }
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get ViewPager2 and Set Adapter
        adapter = ProfileAdapter(pickImageLauncher, requireContext())
        binding.viewPager.setAdapter(adapter)
        binding.viewPager.setCurrentItem(Int.MAX_VALUE / 2, false)
        // Create an object of page transformer
        val cardFlipPageTransformer = CardFlipPageTransformer2()
        cardFlipPageTransformer.isScalable = true
        // Assign the page transformer to the ViewPager2.
        binding.viewPager.setPageTransformer(cardFlipPageTransformer)

        val yourUser = FirebaseAuth.getInstance().currentUser
        //🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄🦄
        adapter.imageUri = yourUser?.photoUrl
        adapter.notifyDataSetChanged()
        binding.usernameEditText.setText(yourUser?.displayName)
        viewModel.loadUser(adapter)



        binding.saveButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val username = binding.usernameEditText.text.toString()
            val bio = binding.bioEditText.text.toString()
            binding.loadingButton.visibility = View.VISIBLE
            binding.saveButton.text = ""

            InputCheck.checkInputsEditProfile(name, username, bio, binding, requireContext()) { error ->
                if (!error) {
                    viewModel.saveUpdates(name, username, bio, adapter.imageUri, requireContext())
                    requireActivity().onBackPressed()
                }
                binding.loadingButton.visibility = View.GONE
                binding.saveButton.text = "Salvar"
            }
        }


        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
