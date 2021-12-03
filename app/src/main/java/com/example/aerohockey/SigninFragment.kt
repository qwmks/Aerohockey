package com.example.aerohockey

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

// TODO: Rename parameter arguments, choose names that match
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SigninFragment : Fragment() {
    private val TAG = "Signing_in"
    private val RC_SIGN_IN = 1234

    private var mSignInClient: GoogleSignInClient? = null
    // Firebase instance variables
    private var mFirebaseAuth: FirebaseAuth? = null
    lateinit var GoogleSignInBut: SignInButton
    lateinit var emailSignInBut: Button
    lateinit var emailRegBut: Button
    lateinit var passwordEditText: EditText
    lateinit var emailEditText: EditText
    lateinit var passwordLayout: TextInputLayout
    lateinit var emailLayout: TextInputLayout
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GoogleSignInBut = view.findViewById(R.id.sign_in_button)
        GoogleSignInBut.setOnClickListener { view -> signInGoogle() }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mSignInClient = getActivity()?.let { GoogleSignIn.getClient(it, gso) }
        mFirebaseAuth = FirebaseAuth.getInstance()
        emailSignInBut = view.findViewById(R.id.signinMailBut)
        emailRegBut = view.findViewById(R.id.registerMailBut)
        passwordLayout = view.findViewById(R.id.passwordLayout)
        emailLayout = view.findViewById(R.id.emailLayout)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        emailEditText = view.findViewById(R.id.emailEditText)
        emailSignInBut.setOnClickListener {
            signInWithEmail(emailEditText.text.toString(),passwordEditText.text.toString())
        }
        emailRegBut.setOnClickListener {
            val action = SigninFragmentDirections.actionSigninFragmentToRegistrationFragment(emailEditText.text.toString(),passwordEditText.text.toString())
            findNavController().navigate(action)
            }


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signin, container, false)
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


                Log.d(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]

        activity?.let {
            mFirebaseAuth?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(it) { task ->
                    var flag:String = "false"
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = mFirebaseAuth?.currentUser

                        updateUI(user)
                        flag = "true"
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(this.context, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        updateUI(null)
                        flag = "true"
                    }

                }
        }
        // [END create_user_with_email]

    }
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(acct!!.idToken, null)
        activity?.let {
            activity?.let { it1 ->
                mFirebaseAuth!!.signInWithCredential(credential)
                    .addOnSuccessListener(it) { authResult: AuthResult? ->

                        Log.d(TAG, "signInWithGoogle:success")
                        mFirebaseAuth!!.currentUser?.email?.let { it2 -> DBHelper.checkExist(it2,::onGoogleSignIn) }

                        Toast.makeText(this.context, "Going to home", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_global_homeFragment)
                        //                finish()
                    }
                    .addOnFailureListener(it1) { e: Exception? ->
                        Log.d(TAG, "signInWithGoogle:failed")
                        Toast.makeText(this.context, "Authentication Failed.", Toast.LENGTH_LONG).show()
                    }
            }
        }
    }
    private fun onGoogleSignIn(res:Boolean){
        if (!res){
            mFirebaseAuth!!.currentUser?.email?.let { it1 -> DBHelper.addUser(it1) }
        }
    }
    private fun signInWithEmail(email: String, password: String) {
        // [START sign_in_with_email]
        activity?.let {
            mFirebaseAuth?.signInWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = mFirebaseAuth?.currentUser
                        findNavController().navigate(R.id.action_global_homeFragment)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this.context, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_global_homeFragment)
                    }
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
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SigninFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SigninFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}