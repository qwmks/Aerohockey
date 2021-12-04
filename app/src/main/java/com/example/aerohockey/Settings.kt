package com.example.aerohockey

import androidx.lifecycle.MutableLiveData


object Settings {
    var field: Int=1
    var striker: Int =0
    var puck:Int = 0
    val money:MutableLiveData<Int>by lazy {
        MutableLiveData<Int>(0)
    }
    var unlockedFields: List<Int> = listOf(0)
    var unlockedStrikers: List<Int> = listOf(0)
    var unlockedPucks: List<Int> = listOf(0)
}