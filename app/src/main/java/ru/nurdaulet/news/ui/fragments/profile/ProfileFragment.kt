package ru.nurdaulet.news.ui.fragments.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.nurdaulet.news.R
import ru.nurdaulet.news.app.NewsApplication
import ru.nurdaulet.news.data.shared_pref.SharedPref
import ru.nurdaulet.news.databinding.FragmentProfileInfoBinding
import ru.nurdaulet.news.ui.ViewModelFactory
import ru.nurdaulet.news.ui.fragments.FragmentGlobalContainer
import ru.nurdaulet.news.ui.fragments.FragmentGlobalContainerDirections
import ru.nurdaulet.news.util.Resource
import javax.inject.Inject

class ProfileFragment : Fragment(R.layout.fragment_profile_info) {

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ProfileViewModel
    private lateinit var parentNavController: NavController

    private var _binding: FragmentProfileInfoBinding? = null
    private val binding: FragmentProfileInfoBinding
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
        _binding = FragmentProfileInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentNavController =
            (parentFragment?.parentFragment as FragmentGlobalContainer).findNavController()
        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
        binding.tvUserName.text = sharedPref.username
        binding.tvUserMail.text = sharedPref.email
        viewModel.getProfileData()
        setupProfileDataObserver()
        setupSignOutObserver()

        binding.apply {
            btnEditProfile.setOnClickListener {
                parentNavController.navigate(R.id.action_fragmentGlobalContainer_to_editProfileFragment)
            }
            btnSettingsProfile.setOnClickListener {
                parentNavController.navigate(R.id.action_fragmentGlobalContainer_to_settingsFragment)
            }
            btnLogOut.setOnClickListener {
                //TODO signOut Observer with sealed class or use FirebaseAuth.AuthStateListener
                viewModel.signOut()
                /*parentNavController.navigate(
                    FragmentGlobalContainerDirections.actionFragmentGlobalContainerToLoginFragment()
                )*/
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
                        }
                        Glide.with(this@ProfileFragment)
                            .load(sharedPref.imageUri)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .placeholder(R.drawable.no_profile_picture)
                            .error(R.drawable.no_profile_picture)
                            .into(binding.profilePicture)
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

    private fun setupSignOutObserver() {
        viewModel.signOutStatus.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    setLoading(false)
                    parentNavController.navigate(FragmentGlobalContainerDirections.actionFragmentGlobalContainerToWelcomeScreen())
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