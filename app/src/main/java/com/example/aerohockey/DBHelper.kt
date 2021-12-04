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
            "coins" to 0,
            "field" to 0,
            "striker" to 0,
            "puck" to 0,
            "availFields" to Settings.unlockedFields,
            "availPucks" to Settings.unlockedPucks,
            "availStrikers" to Settings.unlockedStrikers
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
    fun addMoney(email: String?, coins:Int){
        val db = Firebase.firestore
        var  money = Settings.money.value
        money = money?.plus(coins)
        Settings.money.value=money
        Log.d("Money is",money.toString())
        val userData =hashMapOf(
                "coins" to money,
                "field" to Settings.field,
                "striker" to Settings.striker,
                "puck" to Settings.puck,
                "availFields" to Settings.unlockedFields,
                "availPucks" to Settings.unlockedPucks,
                "availStrikers" to Settings.unlockedStrikers
        )
        if (email != null) {
            db.collection("users").document(email).set(userData)
        }
    }
    fun getSettings(email: String?,callback: (Boolean) -> Unit){
        val db = Firebase.firestore
        if (email != null) {
            db.collection("users").document(email).get().addOnSuccessListener { res->
                if (res.exists()){
                    Settings.money.value=res.get("coins").toString().toInt()
                    Settings.striker=res.get("striker").toString().toInt()
                    Settings.puck=res.get("puck").toString().toInt()
                    Settings.field=res.get("field").toString().toInt()
                    Settings.unlockedPucks = res.get("availFields") as List<Int>
                    Settings.unlockedFields =res.get("availPucks") as List<Int>
                    Settings.unlockedStrikers =res.get("availStrikers") as List<Int>
                }
            }
        }
    }
}