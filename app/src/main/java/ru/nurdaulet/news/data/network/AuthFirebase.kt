package ru.nurdaulet.news.data.network

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class AuthFirebase @Inject constructor(
    //private val auth: FirebaseAuth,
   // private val db: FirebaseFirestore
) {
    private val auth = FirebaseAuth.getInstance()

    fun signUp(
        username: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onSuccess.invoke()
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onSuccess.invoke()
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
}