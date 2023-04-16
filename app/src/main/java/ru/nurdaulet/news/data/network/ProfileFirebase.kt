package ru.nurdaulet.news.data.network

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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
    private val storageRef = FirebaseStorage.getInstance().reference.child("Images")

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

    fun editProfileUsername(
        username: String,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        val user = User(auth.currentUser!!.uid, username, auth.currentUser!!.email!!, sharedPref.imageUri)
        db.collection(Constants.FIREBASE_USERS).document(user.id).set(user)
            .addOnSuccessListener {
                onSuccess.invoke()
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }

    fun uploadProfilePicture(
        imageUri: Uri,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        storageRef.child(auth.currentUser!!.uid).putFile(imageUri)
            .addOnSuccessListener {
                storageRef.child(auth.currentUser!!.uid).downloadUrl.addOnSuccessListener { uri ->
                    sharedPref.imageUri = uri.toString()
                    val user = User(auth.currentUser!!.uid, sharedPref.username, auth.currentUser!!.email!!, uri.toString())
                    db.collection(Constants.FIREBASE_USERS).document(user.id).set(user)
                        .addOnSuccessListener {
                            onSuccess.invoke()
                        }
                        .addOnFailureListener {
                            onFailure.invoke(it.localizedMessage)
                        }
                }
            }.addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
}