package ru.nurdaulet.news.ui.fragments.auth.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import ru.nurdaulet.news.R
import ru.nurdaulet.news.app.NewsApplication
import ru.nurdaulet.news.data.shared_pref.SharedPref
import ru.nurdaulet.news.databinding.FragmentLoginBinding
import ru.nurdaulet.news.ui.ViewModelFactory
import ru.nurdaulet.news.util.Resource
import javax.inject.Inject

class LoginFragment : Fragment(R.layout.fragment_login) {

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: LoginViewModel

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding ?: throw RuntimeException("binding == null")

    private lateinit var signInClient: GoogleSignInClient
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO navcontroller
        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

        initGoogleSignInOptions()
        binding.apply {
            btnGoogleSignIn.setOnClickListener {
                signInClient.signInIntent.also {
                    resultLauncher.launch(it)
                }
            }
            btnLogin.setOnClickListener {
                if (validateLoginInput()) {
                    viewModel.login(etEmail.text.toString(), etPassword.text.toString())
                }
            }

            tvSignUp.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
            }
        }
        setupLoginObserver()
    }

    private fun initGoogleSignInOptions() {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_auth_id))
            .requestEmail()
            .build()
        signInClient = GoogleSignIn.getClient(requireActivity(), options)

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
                    account?.let {
                        viewModel.googleSignIn(it, signInClient)
                    }
                }
            }
    }

    private fun setupLoginObserver() {
        viewModel.apply {
            loginStatus.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Success -> {
                        setLoading(false)
                        sharedPref.isSigned = true
                        sharedPref.isGoogleSigned = false
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToFragmentGlobalContainer())
                    }
                    is Resource.Error -> {
                        setLoading(false)
                        response.message?.let { message ->
                            Toast.makeText(
                                activity,
                                "An error occurred: $message",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                    is Resource.Loading -> {
                        setLoading(true)
                    }
                }
            }
            googleSignInStatus.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Success -> {
                        setLoading(false)
                        sharedPref.isGoogleSigned = true
                        sharedPref.isSigned = false
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToFragmentGlobalContainer())
                    }
                    is Resource.Error -> {
                        setLoading(false)
                        response.message?.let { message ->
                            Toast.makeText(
                                activity,
                                "An error occurred: $message",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                    is Resource.Loading -> {
                        setLoading(true)
                    }
                }
            }
        }
    }

    private fun validateLoginInput(): Boolean {
        binding.apply {
            val emailIsNotEmpty = etEmail.text!!.isNotEmpty()
            val passwordIsNotEmpty = etPassword.text!!.isNotEmpty()
            val passwordLengthIsValid = etPassword.length() >= 8
            //TODO error observer text and state changes

            return if (emailIsNotEmpty
                && passwordIsNotEmpty
                && passwordLengthIsValid
            ) {
                true
            } else if (!emailIsNotEmpty) {
                tilEmail.error = getString(R.string.email_is_empty)
                false
            } else if (!passwordIsNotEmpty) {
                tilPassword.error = getString(R.string.password_is_empty)
                false
            } else if (!passwordLengthIsValid) {
                tilPassword.error = getString(R.string.password_length_condition)
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
            etPassword.isEnabled = !isLoading
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}