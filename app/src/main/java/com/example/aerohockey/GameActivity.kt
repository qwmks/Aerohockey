package com.example.aerohockey

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        supportActionBar?.hide()
//        score = findViewById(R.id.scoreText)
//        Settings.field=0
//        Settings.striker=1
//        gameView = GameView(this,null)
    }
}