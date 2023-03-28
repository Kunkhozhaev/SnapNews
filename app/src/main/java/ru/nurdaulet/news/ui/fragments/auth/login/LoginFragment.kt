package ru.nurdaulet.news.ui.fragments.auth.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.nurdaulet.news.R
import ru.nurdaulet.news.app.NewsApplication
import ru.nurdaulet.news.databinding.FragmentLoginBinding
import ru.nurdaulet.news.ui.ViewModelFactory
import ru.nurdaulet.news.util.Resource
import javax.inject.Inject

class LoginFragment : Fragment(R.layout.fragment_login) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: LoginViewModel

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding ?: throw RuntimeException("binding == null")

    private lateinit var auth: FirebaseAuth
    private lateinit var signInClient: GoogleSignInClient

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

        auth = FirebaseAuth.getInstance()
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_auth_id))
            .requestEmail()
            .build()
        signInClient = GoogleSignIn.getClient(requireActivity(), options)

        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                Log.d("LoginFragment", "I am not succeeded yet ${result.resultCode}")
                if (result.resultCode == Activity.RESULT_OK) {
                    Log.d("LoginFragment", "I am in resulLauncher ")
                    // There are no request codes
                    val data: Intent? = result.data
                    val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
                    account?.let {
                        Log.d("LoginFragment", it.toString())
                        googleAuthForFirebase(it)
                    }
                }
            }

        binding.apply {
            btnGoogleSignIn.setOnClickListener {
                Log.d("LoginFragment", "Button is clicked ")
                signInClient.signInIntent.also {
                    Log.d("LoginFragment", "Sign in intent ")
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

    private fun setupLoginObserver() {
        viewModel.loginStatus.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    setLoading(false)
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToFragmentGlobalContainer())
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

    private fun googleAuthForFirebase(account: GoogleSignInAccount) {
        setLoading(true)
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.signInWithCredential(credentials).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToFragmentGlobalContainer())
                        Toast.makeText(requireContext(), "Success Google", Toast.LENGTH_SHORT)
                            .show()
                        setLoading(false)
                    } else {
                        Toast.makeText(requireContext(), task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validateLoginInput(): Boolean {
        binding.apply {
            val emailIsNotEmpty = etEmail.text!!.isNotEmpty()
            val passwordIsNotEmpty = etPassword.text!!.isNotEmpty()
            val passwordLengthIsValid = etPassword.length() >= 8
            //TODO error observers

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