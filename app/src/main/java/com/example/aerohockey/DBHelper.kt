package com.example.aerohockey

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


const val TAG = "DBHelper"
object DBHelper {
    fun checkExist(email: String, callback: (Boolean) -> Unit) {
        val db = Firebase.firestore
        db.collection("users").document(email).get().addOnSuccessListener { result ->
            if (result.exists()){
                callback(true)
            }
            else{
                callback(false)
            }
        }
    }
    fun saveSettings(email: String){
        val db = Firebase.firestore
        val userData =hashMapOf(
                "coins" to Settings.money.value,
                "field" to Settings.field,
                "striker" to Settings.striker,
                "puck" to Settings.puck,
                "availFields" to Settings.unlockedFields,
                "availPucks" to Settings.unlockedPucks,
                "availStrikers" to Settings.unlockedStrikers
        )
        db.collection("users").document(email).set(userData)
    }
    fun makePurchase(email: String,cost:Int,type:Int,id:Int, callback: (Boolean) -> Unit){
        val db = Firebase.firestore
        Settings.money.value = Settings.money.value?.minus(cost)
        when(type){
            1->Settings.unlockedFields.plusAssign(id)
            2->Settings.unlockedPucks.plusAssign(id)
            3->Settings.unlockedStrikers.plusAssign(id)
        }
        val userData =hashMapOf(
                "coins" to Settings.money.value,
                "field" to Settings.field,
                "striker" to Settings.striker,
                "puck" to Settings.puck,
                "availFields" to Settings.unlockedFields,
                "availPucks" to Settings.unlockedPucks,
                "availStrikers" to Settings.unlockedStrikers
        )
        db.collection("users").document(email).set(userData).addOnSuccessListener {
            callback(true)
        }
                .addOnFailureListener {
                    callback(false)
                }
    }
    fun addUser(email: String) {
        val db = Firebase.firestore
        val userData =hashMapOf(
                "coins" to 0,
                "field" to 0,
                "striker" to 0,
                "puck" to 0,
                "availFields" to listOf<Int>(0),
                "availPucks" to listOf<Int>(0),
                "availStrikers" to listOf<Int>(0)
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
    fun addMoney(email: String?, coins: Int, callback: (Boolean) -> Unit){
        val db = Firebase.firestore
        var  money = Settings.money.value
        money = money?.plus(coins)
        Settings.money.value=money
        Log.d("Money is", money.toString())
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
            db.collection("users").document(email).set(userData).addOnSuccessListener {
                callback(true)
            }
                    .addOnFailureListener {
                        callback(false)
                    }
        }
    }
    fun getSettings(email: String?, callback: (Boolean) -> Unit){
        val db = Firebase.firestore
        if (email != null) {
            db.collection("users").document(email).get().addOnSuccessListener { res->
                if (res.exists()){
                    Log.d(TAG, "DocumentSnapshot data: ${res.data}")
                    Settings.money.value=res.get("coins").toString().toInt()
                    Settings.striker=res.get("striker").toString().toInt()
                    Settings.puck=res.get("puck").toString().toInt()
                    Settings.field=res.get("field").toString().toInt()
                    var unlockedFields: List<Int> = listOf(0)
                    unlockedFields= listOf(0,1)
                    Log.d("Reassigning list",unlockedFields.toString())
                    val temp = res.get("availFields")
                    Log.d(TAG,temp.toString())

                    temp!!::class.qualifiedName?.let { Log.d(TAG, it) }
                    Settings.unlockedPucks::class.qualifiedName?.let { Log.d("In settings", it) }

                    Settings.unlockedFields = (res.get("availFields") as List<Int>).toMutableList()
                    Settings.unlockedPucks = (res.get("availPucks") as List<Int>).toMutableList()
                    Settings.unlockedStrikers = (res.get("availStrikers") as List<Int>).toMutableList()
                    Log.d(TAG,res.get("availPucks").toString())
                    Log.d("Result is", Settings.unlockedPucks.toString())
                    callback(true)
                }
            }
                    .addOnFailureListener {
                        callback(false)
                    }
        }
    }
}