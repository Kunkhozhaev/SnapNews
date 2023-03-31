package ru.nurdaulet.news.ui.fragments.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.nurdaulet.news.R
import ru.nurdaulet.news.app.NewsApplication
import ru.nurdaulet.news.databinding.FragmentEditProfileBinding
import ru.nurdaulet.news.ui.ViewModelFactory
import ru.nurdaulet.news.util.Resource
import javax.inject.Inject

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: EditProfileViewModel

    private var _binding: FragmentEditProfileBinding? = null
    private val binding: FragmentEditProfileBinding
        get() = _binding ?: throw RuntimeException("binding == null")

    private val component by lazy {
        (requireActivity().application as NewsApplication).component
    }

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
        viewModel.getProfileData()
        setupProfileDataObserver()

        binding.apply {
            iconBack.setOnClickListener {
                findNavController().popBackStack()
            }
            ivConfirmEditBtn.setOnClickListener {
                //TODO Edit  Profile
                Toast.makeText(requireActivity(), "Profile saved", Toast.LENGTH_SHORT).show()
            }
            icUploadPicture.setOnClickListener {
                //TODO Load picture
                Toast.makeText(requireActivity(), "Uploaded", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupProfileDataObserver() {
        viewModel.profileStatus.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    binding.apply {
                        response.data?.let { user ->
                            tvUserName.text = user.username
                            tvUserMail.text = user.email

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}