package com.example.aerohockey

import androidx.lifecycle.MutableLiveData


object Settings {
    var field: Int=1
    var striker: Int =0
    var puck:Int = 0
    val money:MutableLiveData<Int>by lazy {
        MutableLiveData<Int>(0)
    }
    var unlockedFields:MutableList<Int> = mutableListOf(0)
    var unlockedStrikers:MutableList<Int> = mutableListOf(0)
    var unlockedPucks:MutableList<Int> = mutableListOf(0)
}