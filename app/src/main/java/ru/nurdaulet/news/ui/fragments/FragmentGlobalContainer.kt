package ru.nurdaulet.news.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import ru.nurdaulet.news.R
import ru.nurdaulet.news.databinding.FragmentGlobalContainerBinding

class FragmentGlobalContainer : Fragment(R.layout.fragment_global_container) {

    private lateinit var navController: NavController

    private var _binding: FragmentGlobalContainerBinding? = null

    private val binding: FragmentGlobalContainerBinding
        get() = _binding ?: throw RuntimeException("binding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGlobalContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*navHostFragment = requireActivity().supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment*/
        /*val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)*/

        navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
        binding.bottomNavigationView.setupWithNavController(navController)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}