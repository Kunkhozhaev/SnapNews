package ru.nurdaulet.news.data.shared_pref

interface SharedPref {
    var isSigned: Boolean
    var isGoogleSigned: Boolean
    var country: String
    var username: String
    var email: String

    /*var isSigned: Boolean by BooleanPreference(pref, false)
    var isGoogleSigned: Boolean by BooleanPreference(pref, false)
    var country: String by StringPreference(pref, "ru")
    var username: String by StringPreference(pref, "")
    var email: String by StringPreference(pref, "")*/
}