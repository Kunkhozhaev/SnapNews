package ru.nurdaulet.news.data.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ru.nurdaulet.news.data.shared_pref.SharedPref
import ru.nurdaulet.news.domain.models.User
import ru.nurdaulet.news.util.Constants
import javax.inject.Inject

class ProfileFirebase @Inject constructor(
    private val sharedPref: SharedPref
    //private val auth: FirebaseAuth,
    // private val db: FirebaseFirestore
) {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun getProfileData(
        onSuccess: (user: User) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        db.collection(Constants.FIREBASE_USERS).document(auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                val result = it.toObject(User::class.java)
                result?.let { user ->
                    sharedPref.username = user.username
                    sharedPref.email = user.email
                    onSuccess.invoke(user)
                } ?: onFailure.invoke("User data is empty")
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
}