package com.example.aerohockey

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
const val TAG = "DBHelper"
object DBHelper {
    fun checkExist(email: String, callback: (Boolean) -> Unit) {
        val db = Firebase.firestore
        db.collection("users").document(email).get().addOnSuccessListener {result ->
            if (result.exists()){
                callback(true)
            }
            else{
                callback(false)
            }
        }
    }

    fun addUser(email: String) {
        val db = Firebase.firestore
        val userData =hashMapOf(
            "coins" to 1
        )
        db.collection("users").document(email)
                .set(userData)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: $documentReference")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
    }
}