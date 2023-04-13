package ru.nurdaulet.news.ui.splash_screen

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withResumed
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.nurdaulet.news.R
import ru.nurdaulet.news.app.NewsApplication
import ru.nurdaulet.news.data.shared_pref.SharedPref
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


class SplashScreen : Fragment(R.layout.fragment_splash_screen) {

    @Inject
    lateinit var sharedPref: SharedPref

    private val navController by lazy { findNavController() }
    private val component by lazy {
        (requireActivity().application as NewsApplication).component
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            delay(1.seconds)
            lifecycle.withResumed {
                if (sharedPref.isSigned || sharedPref.isGoogleSigned) {
                    navController.navigate(SplashScreenDirections.actionSplashScreenToFragmentGlobalContainer())
                } else {
                    navController.navigate(SplashScreenDirections.actionSplashScreenToWelcomeScreen())
                }
            }
        }
    }
}