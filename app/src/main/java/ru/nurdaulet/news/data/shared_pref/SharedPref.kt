package ru.nurdaulet.news.data.shared_pref

interface SharedPref {
    var isSigned: Boolean
    var isGoogleSigned: Boolean
    var country: String
    var username: String
    var email: String
    var imageUri: String
}