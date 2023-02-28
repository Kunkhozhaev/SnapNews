package ru.nurdaulet.news.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.nurdaulet.news.R
import ru.nurdaulet.news.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding: FragmentEditProfileBinding
        get() = _binding ?: throw RuntimeException("binding == null")

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

        binding.apply {
            iconBack.setOnClickListener {
                findNavController().popBackStack()
            }
            ivConfirmEditBtn.setOnClickListener {
                Toast.makeText(requireActivity(), "Profile saved", Toast.LENGTH_SHORT).show()
            }
            icUploadPicture.setOnClickListener {
                Toast.makeText(requireActivity(), "Uploaded", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}