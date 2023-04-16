package ru.nurdaulet.news.data.shared_pref

import android.content.Context
import android.content.SharedPreferences
import ru.nurdaulet.news.util.Constants
import javax.inject.Inject

class SharedPrefImpl @Inject constructor(context: Context) : SharedPref {

    private val pref: SharedPreferences =
        context.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE)
    private var editor = pref.edit()

    override var username: String
        get() = getString(USER_NAME_KEY)
        set(value) {
            saveString(USER_NAME_KEY, value)
        }

    override var isSigned: Boolean
        get() = getBoolean(IS_SIGNED_KEY)
        set(value) {
            saveBoolean(IS_SIGNED_KEY, value)
        }

    override var isGoogleSigned: Boolean
        get() = getBoolean(IS_GOOGLE_KEY)
        set(value) {
            saveBoolean(IS_GOOGLE_KEY, value)
        }

    override var country: String
        get() = getString(COUNTRY_KEY)
        set(value) {
            saveString(COUNTRY_KEY, value)
        }

    override var email: String
        get() = getString(EMAIL_KEY)
        set(value) {
            saveString(EMAIL_KEY, value)
        }

    override var imageUri: String
        get() = getString(IMAGE_KEY)
        set(value) {
            saveString(IMAGE_KEY, value)
        }

    private fun saveString(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    private fun getString(key: String, defaultValue: String = ""): String {
        return pref.getString(key, defaultValue) ?: defaultValue
    }

    private fun saveBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value).apply()
    }

    private fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return pref.getBoolean(key, defaultValue)
    }

    companion object {
        private const val IS_SIGNED_KEY = "is_signed"
        private const val IS_GOOGLE_KEY = "is_google_signed"
        private const val USER_NAME_KEY = "username"
        private const val EMAIL_KEY = "email"
        private const val COUNTRY_KEY = "country"
        private const val IMAGE_KEY = "image"
    }
}