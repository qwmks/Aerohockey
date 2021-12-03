package com.example.aerohockey

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {
    private var gameView: GameView? = null
    lateinit var score:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(DrawView(this))
        setContentView(R.layout.activity_game)
        supportActionBar?.hide()
        score = findViewById(R.id.scoreText)
        Settings.field=0
        Settings.striker=1
        gameView = GameView(this,null)
    }
}