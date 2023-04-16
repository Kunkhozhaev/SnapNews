package ru.nurdaulet.news.data.network

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.nurdaulet.news.data.shared_pref.SharedPref
import ru.nurdaulet.news.domain.models.User
import ru.nurdaulet.news.util.Constants.FIREBASE_USERS
import ru.nurdaulet.news.util.Constants.currentSignInClient
import javax.inject.Inject

class AuthFirebase @Inject constructor(
    private val sharedPref: SharedPref
    //private val auth: FirebaseAuth,
    // private val db: FirebaseFirestore
) {
    // TODO(Inject these)
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
        account: GoogleSignInAccount,
        signInClient: GoogleSignInClient,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        currentSignInClient.add(signInClient)
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.signInWithCredential(credentials).addOnSuccessListener {
                    db.collection(FIREBASE_USERS).document(auth.currentUser!!.uid).get()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val doc = task.result
                                if (doc.exists()) {
                                    onSuccess.invoke()
                                } else {
                                    val user = User(
                                        auth.currentUser!!.uid,
                                        account.displayName!!,
                                        account.email!!,
                                        ""
                                    )
                                    db.collection(FIREBASE_USERS).document(user.id).set(user)
                                    onSuccess.invoke()
                                }
                            } else {
                                onFailure.invoke(task.exception?.message)
                            }
                        }
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
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
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
        db.collection(FIREBASE_USERS).document(user.id).set(user)
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
        //TODO (signOut value store. Maybe in shared pref?)
        if (sharedPref.isSigned) {
            //TODO (FirebaseAuth signOut listneres)
            auth.signOut()
            sharedPref.username = ""
            sharedPref.email = ""
            sharedPref.isSigned = false
            sharedPref.imageUri = ""
        } else if (sharedPref.isGoogleSigned) {
            currentSignInClient[0].signOut().addOnSuccessListener {
                onSuccess.invoke()
            }.addOnFailureListener { exception ->
                onFailure.invoke(exception.localizedMessage)
            }.addOnCompleteListener {
                currentSignInClient.clear()
                sharedPref.username = ""
                sharedPref.email = ""
                sharedPref.imageUri = ""
                sharedPref.isGoogleSigned = false
            }
        }
    }
}