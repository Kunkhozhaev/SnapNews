package ru.nurdaulet.news.data.network

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        account: GoogleSignInAccount, onSuccess: () -> Unit, onFailure: (msg: String?) -> Unit
    ) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.signInWithCredential(credentials).addOnSuccessListener {
                    val user =
                        User(auth.currentUser!!.uid, account.displayName!!, account.email!!, "")
                    db.collection(Constants.FIREBASE_USERS).document(user.id).set(user)
                    onSuccess.invoke()
                }.addOnFailureListener {
                    onFailure.invoke(it.localizedMessage)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onFailure.invoke(e.toString())
                }
            }
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