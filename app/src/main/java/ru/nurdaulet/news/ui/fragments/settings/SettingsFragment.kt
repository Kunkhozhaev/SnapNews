package ru.nurdaulet.news.ui.fragments.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.nurdaulet.news.R
import ru.nurdaulet.news.app.NewsApplication
import ru.nurdaulet.news.data.shared_pref.SharedPref
import ru.nurdaulet.news.databinding.FragmentSettingsBinding
import ru.nurdaulet.news.ui.ViewModelFactory
import ru.nurdaulet.news.util.Constants
import ru.nurdaulet.news.util.Constants.listOfAbbreviations
import ru.nurdaulet.news.util.Resource
import javax.inject.Inject

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: SettingsViewModel
    private var countryIndex = 0

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
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
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[SettingsViewModel::class.java]
        binding.apply {
            iconBack.setOnClickListener {
                findNavController().popBackStack()
            }
            countrySelector.setOnItemClickListener { adapterView, view, index, id ->
                viewModel.editCountry(listOfAbbreviations[index])
                countryIndex = index
            }
        }
        setupCountrySelector()
        setupObservers()
    }

    private fun setupCountrySelector() {
        val countriesAdapter =
            ArrayAdapter(
                requireActivity(),
                R.layout.item_country_dropdown,
                Constants.listOfCountries
            )
        binding.countrySelector.setAdapter(countriesAdapter)
    }

    private fun setupObservers() {
        viewModel.editCountryStatus.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    setLoading(false)
                    binding.apply {
                        Toast.makeText(
                            requireActivity(),
                            "Updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        sharedPref.country = listOfAbbreviations[countryIndex]
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