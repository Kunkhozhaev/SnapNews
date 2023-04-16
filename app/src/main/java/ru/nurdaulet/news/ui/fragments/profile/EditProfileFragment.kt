package ru.nurdaulet.news.ui.fragments.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import ru.nurdaulet.news.R
import ru.nurdaulet.news.app.NewsApplication
import ru.nurdaulet.news.data.shared_pref.SharedPref
import ru.nurdaulet.news.databinding.FragmentEditProfileBinding
import ru.nurdaulet.news.ui.ViewModelFactory
import ru.nurdaulet.news.util.Resource
import java.io.IOException
import javax.inject.Inject

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: EditProfileViewModel

    private var _binding: FragmentEditProfileBinding? = null
    private val binding: FragmentEditProfileBinding
        get() = _binding ?: throw RuntimeException("binding == null")

    private val component by lazy {
        (requireActivity().application as NewsApplication).component
    }

    private lateinit var resultLauncher: ActivityResultLauncher<String>

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[EditProfileViewModel::class.java]
        binding.apply {
            tvUserName.text = sharedPref.username
            tvUserMail.text = sharedPref.email
            etFullName.setText(sharedPref.username)
            etEmail.setText(sharedPref.email)
            iconBack.setOnClickListener {
                findNavController().popBackStack()
            }
            ivConfirmEditBtn.setOnClickListener {
                editProfile()
            }
            icUploadPicture.setOnClickListener {
                //TODO Load picture
                launchGallery()
                Toast.makeText(requireActivity(), "Uploaded", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.getProfileData()
        setupObservers()
        initActivityResultLauncher()
    }

    private fun launchGallery() {
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.type = "image/*"
        resultLauncher.launch("image/*")
    }

    private fun initActivityResultLauncher() {
        resultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let {
                Log.d("ProfileFirebase", it.toString())
                viewModel.uploadPicture(it)
            }
        }

        /*resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    data?.let {
                        val filePathUri = it.data
                        try {
                            if (Build.VERSION.SDK_INT <= 28) {
                                // TODO(Deprecated)
                                val bitmap = MediaStore.Images.Media.getBitmap(
                                    requireActivity().contentResolver,
                                    filePathUri
                                )
                                binding.profilePicture.setImageBitmap(bitmap)
                            } else {
                                val source = ImageDecoder.createSource(
                                    requireActivity().contentResolver,
                                    filePathUri!!
                                )
                                binding.profilePicture.setImageBitmap(
                                    ImageDecoder.decodeBitmap(
                                        source
                                    )
                                )
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }*/
    }

    private fun editProfile() {
        val username = binding.etFullName.text.toString()
        if (username.isNotEmpty()) {
            binding.tilFullName.error = null
            viewModel.editProfileData(username)
        } else {
            binding.tilFullName.error = getString(R.string.field_is_empty)
        }
    }

    private fun setupObservers() {
        viewModel.profileStatus.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    binding.apply {
                        response.data?.let { user ->
                            tvUserName.text = user.username
                            tvUserMail.text = user.email

                            Glide.with(this@EditProfileFragment)
                                .load(user.image)
                                .error(R.drawable.no_profile_picture)
                                .into(binding.profilePicture)

                            etFullName.setText(user.username)
                            etEmail.setText(user.email)
                        }
                    }
                }

                is Resource.Error -> {
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                is Resource.Loading -> {
                }
            }
        }
        viewModel.editProfileStatus.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    setLoading(false)
                    binding.apply {
                        Toast.makeText(
                            requireActivity(),
                            "Updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        sharedPref.username = binding.etFullName.text.toString()
                        tvUserName.text = binding.etFullName.text.toString()
                    }
                }

                is Resource.Error -> {
                    setLoading(false)
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                is Resource.Loading -> {
                    setLoading(true)
                }
            }
        }
        viewModel.uploadImageStatus.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    setLoading(false)
                    Toast.makeText(
                        requireActivity(),
                        "Image loaded successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.getProfileData()
                }
                is Resource.Error -> {
                    setLoading(false)
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Resource.Loading -> {
                    setLoading(true)
                }
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.apply {
            progressBar.isVisible = isLoading
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}