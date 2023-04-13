package ru.nurdaulet.news.ui.fragments.auth.signup

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.nurdaulet.news.R
import ru.nurdaulet.news.app.NewsApplication
import ru.nurdaulet.news.databinding.FragmentSignUpBinding
import ru.nurdaulet.news.ui.ViewModelFactory
import ru.nurdaulet.news.util.Resource
import javax.inject.Inject

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {
    private var _binding: FragmentSignUpBinding? = null
    private val binding: FragmentSignUpBinding
        get() = _binding ?: throw RuntimeException("binding == null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: SignUpViewModel

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
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[SignUpViewModel::class.java]

        binding.apply {
            iconBack.setOnClickListener {
                findNavController().popBackStack()
            }

            btnSignUp.setOnClickListener {
                if (validateSignUpInput()) {
                    viewModel.signUp(
                        //etUserName.text.toString(),
                        etEmail.text.toString(),
                        etPassword.text.toString()
                    )
                }
            }

            tvLogin.setOnClickListener {
                findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
            }
        }

        setupSignUpObserver()
    }

    private fun setupSignUpObserver() {
        viewModel.signUpStatus.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    setLoading(false)
                    viewModel.addUserToDb(binding.etUserName.text.toString())
                    setupAddUserObserver()
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
    private fun setupAddUserObserver() {
        viewModel.userAddStatus.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    setLoading(false)
                    findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
                }
                is Resource.Error -> {
                    setLoading(false)
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_SHORT)
                            .show()
                        Log.d("SignUpFragment", message)
                    }
                }
                is Resource.Loading -> {
                    setLoading(true)
                }
            }
        }
    }

    private fun validateSignUpInput(): Boolean {
        binding.apply {
            val usernameIsNotEmtpy = etUserName.text!!.isNotEmpty()
            val emailIsNotEmpty = etEmail.text!!.isNotEmpty()
            val passwordIsNotEmpty = etPassword.text!!.isNotEmpty()
            val passwordLengthIsValid = etPassword.length() >= 8
            val passwordsMatch = etPassword.text.toString() == etConfirmPassword.text.toString()
            //TODO error observers

            return if (usernameIsNotEmtpy
                && emailIsNotEmpty
                && passwordIsNotEmpty
                && passwordLengthIsValid
                && passwordsMatch
            ) {
                true
            } else if (!usernameIsNotEmtpy) {
                tilUserName.error = getString(R.string.username_is_empty)
                false
            } else if (!emailIsNotEmpty) {
                tilEmail.error = getString(R.string.email_is_empty)
                false
            } else if (!passwordIsNotEmpty) {
                tilPassword.error = getString(R.string.password_is_empty)
                false
            } else if (!passwordLengthIsValid) {
                tilPassword.error = getString(R.string.password_length_condition)
                false
            } else if (!passwordsMatch) {
                tilConfirmPassword.error = getString(R.string.password_match_condition)
                false
            } else {
                false
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.apply {
            progressBar.isVisible = isLoading
            etEmail.isEnabled = !isLoading
            etUserName.isEnabled = !isLoading
            etPassword.isEnabled = !isLoading
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}