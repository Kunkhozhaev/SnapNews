package ru.nurdaulet.news.ui.fragments.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.nurdaulet.news.R
import ru.nurdaulet.news.databinding.FragmentWelcomeScreenBinding


class WelcomeFragment : Fragment(R.layout.fragment_welcome_screen) {

    private var _binding: FragmentWelcomeScreenBinding? = null
    private val binding: FragmentWelcomeScreenBinding
        get() = _binding ?: throw RuntimeException("binding == null")

    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            navController.navigate(WelcomeFragmentDirections.actionWelcomeScreenToLoginFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}