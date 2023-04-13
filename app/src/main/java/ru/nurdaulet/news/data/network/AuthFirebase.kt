package ru.nurdaulet.news.data.network

import android.util.Log
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
import ru.nurdaulet.news.util.Constants
import ru.nurdaulet.news.util.Constants.currentSignInClient
import javax.inject.Inject

class AuthFirebase @Inject constructor(
    private val sharedPref: SharedPref
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
        account: GoogleSignInAccount,
        signInClient: GoogleSignInClient,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        currentSignInClient.add(signInClient)
        Log.d("Blablabla", "This is $signInClient \n ${currentSignInClient[0]}")
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
        Log.d("Blablabla", "This is $currentSignInClient")
        //TODO (signOut value store. Maybe in shared pref?)
        if (sharedPref.isSigned) {
            //TODO (FirebaseAuth signOut listneres)
            auth.signOut()
            sharedPref.username = ""
            sharedPref.email = ""
        } else if (sharedPref.isGoogleSigned) {
            currentSignInClient[0].signOut().addOnSuccessListener {
                onSuccess.invoke()
                sharedPref.username = ""
                sharedPref.email = ""
            }.addOnFailureListener { exception ->
                onFailure.invoke(exception.localizedMessage)
            }.addOnCompleteListener {
                currentSignInClient.clear()
            }
        }
    }
}