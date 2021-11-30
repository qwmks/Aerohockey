package com.example.aerohockey


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var text:TextView
    lateinit var signOut: Button
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val fm: FragmentManager = supportFragmentManager
        setContentView(R.layout.activity_main)
        //test
//        auth = FirebaseAuth.getInstance()
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getResources().getString(R.string.web_id))
//                .requestEmail()
//                .build()
//        setContentView(R.layout.activity_main)
//        googleSignInClient = GoogleSignIn.getClient(this, gso)
//        signOut = findViewById(R.id.sign_out)
//        signOut.setOnClickListener {
//            auth.signOut()
//            googleSignInClient.signOut()
//            findNavController().navigate(R.id.action_global_signinFragment)
        }



//    }
//    override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser: FirebaseUser? = auth.getCurrentUser()
//        updateUI(currentUser)
//    }
//    private fun updateUI(user: FirebaseUser?) {
//        text = findViewById(R.id.welcomeText)
//        if (user != null) {
//            text.setText(user.displayName)
//        }
//    }
}

