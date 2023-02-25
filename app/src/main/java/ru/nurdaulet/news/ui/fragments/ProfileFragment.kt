package ru.nurdaulet.news.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import ru.nurdaulet.news.R
import ru.nurdaulet.news.databinding.FragmentProfileInfoBinding
import ru.nurdaulet.news.ui.NewsActivity
import ru.nurdaulet.news.ui.NewsViewModel

class ProfileFragment : Fragment(R.layout.fragment_profile_info) {

    lateinit var viewModel: NewsViewModel

    private lateinit var parentNavController: NavController

    private var _binding: FragmentProfileInfoBinding? = null
    private val binding: FragmentProfileInfoBinding
        get() = _binding ?: throw RuntimeException("binding == null")


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

        binding.btnEditProfile.setOnClickListener {
            parentNavController = (parentFragment?.parentFragment as FragmentGlobalContainer).findNavController()
            parentNavController.navigate(R.id.action_fragmentGlobalContainer_to_editProfileFragment)
            //findNavController().navigate(R.id.action_fragmentGlobalContainer_to_editProfileFragment)
        }

        viewModel = (activity as NewsActivity).viewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}