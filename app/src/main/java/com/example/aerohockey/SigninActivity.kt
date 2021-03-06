package com.example.aerohockey

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


class SigninActivity : AppCompatActivity() {
    private val TAG = "SignInActivity"
    private val RC_SIGN_IN = 1234

    private var mSignInClient: GoogleSignInClient? = null
    // Firebase instance variables
    private var mFirebaseAuth: FirebaseAuth? = null
    lateinit var signinButton: SignInButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        signinButton = findViewById(R.id.sign_in_button)
        signinButton.setOnClickListener { view -> signInGoogle() }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mSignInClient = GoogleSignIn.getClient(this, gso)
        mFirebaseAuth = FirebaseAuth.getInstance()
    }

    private fun signInGoogle() {
        val signInIntent = mSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {


                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }
    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        mFirebaseAuth?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = mFirebaseAuth?.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
        // [END create_user_with_email]
    }
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(acct!!.idToken, null)
        mFirebaseAuth!!.signInWithCredential(credential)
                .addOnSuccessListener(this) { authResult: AuthResult? ->
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                .addOnFailureListener(this) { e: Exception? ->
                    Toast.makeText(applicationContext, "Authentication Failed.", Toast.LENGTH_LONG).show()
                }
    }
    private fun signInWithEmail(email: String, password: String) {
        // [START sign_in_with_email]
        mFirebaseAuth?.signInWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = mFirebaseAuth?.currentUser
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
        // [END sign_in_with_email]
    }
    private fun updateUI(user: FirebaseUser?) {

    }
//    private fun sendEmailVerification() {
//        // [START send_email_verification]
//        val user = mFirebaseAuth.currentUser!!
//        user.sendEmailVerification()
//                .addOnCompleteListener(this) { task ->
//                    // Email Verification sent
//                }
//        // [END send_email_verification]
//    }
}