package com.example.aerohockey

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val bottomNavigationView = findViewById<BottomNavigationView
                >(R.id.bottom_navigation_view)
        val navController = findNavController(R.id.nav_fragment_settings)
        bottomNavigationView.setupWithNavController(navController
        )
        val moneyAmount:TextView = findViewById(R.id.moneyAmount)
        val moneyObserver = Observer<Int> { newValue ->
            // Update the UI, in this case, a TextView. {
            moneyAmount.text = newValue.toString()
        }
        Settings.money.observe(this, moneyObserver)
        val fab:FloatingActionButton = findViewById(R.id.fabBack)
        fab.setOnClickListener {
            findNavController(R.id.nav_fragment_settings).navigate(R.id.action_global_mainactivity)
        }
    }
}