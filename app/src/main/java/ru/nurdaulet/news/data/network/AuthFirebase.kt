package ru.nurdaulet.news.data.network

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ru.nurdaulet.news.domain.models.User
import ru.nurdaulet.news.util.Constants
import javax.inject.Inject

class AuthFirebase @Inject constructor(
    //private val auth: FirebaseAuth,
    // private val db: FirebaseFirestore
) {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun signUp(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            onSuccess.invoke()
        }.addOnFailureListener {
            onFailure.invoke(it.localizedMessage)
        }
    }

    fun googleSignIn(
        credentials: AuthCredential, onSuccess: () -> Unit, onFailure: (msg: String?) -> Unit
    ) {
        auth.signInWithCredential(credentials).addOnSuccessListener {
            onSuccess.invoke()
        }.addOnFailureListener {
            onFailure.invoke(it.localizedMessage)
        }
    }

    fun login(
        email: String, password: String, onSuccess: () -> Unit, onFailure: (msg: String?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            onSuccess.invoke()
        }.addOnFailureListener {
            onFailure.invoke(it.localizedMessage)
        }
    }

    fun addUserToDb(
        username: String,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        val user = User(auth.currentUser!!.uid, username, auth.currentUser!!.email!!, "")
        db.collection(Constants.FIREBASE_USERS).document(user.id).set(user)
            .addOnSuccessListener {
                onSuccess.invoke()
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }

    fun signOut(
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        auth.signOut()
    }
}