package com.example.aerohockey

import android.R.attr.name
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegistrationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistrationFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private val TAG = "Registration"
    lateinit var password: TextView
    lateinit var email: TextView
    lateinit var nickname: TextView
    lateinit var registerButton: Button
    private var mFirebaseAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args: RegistrationFragmentArgs by navArgs()
        mFirebaseAuth = FirebaseAuth.getInstance()
        password= view.findViewById(R.id.passwordEditText)
        email = view.findViewById(R.id.emailEditText)
        registerButton= view.findViewById(R.id.registerMailBut)
        nickname = view.findViewById(R.id.nicknameEditText)
        password.text=args.password
        email.text=args.email
        registerButton.setOnClickListener { createAccount(email.text.toString(),
            password.text.toString()
        ) }
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
                        mFirebaseAuth!!.currentUser?.email?.let { it1 -> DBHelper.addUser(it1) }
                        updateUI(user)
                        flag = "true"
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            this.context, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                        flag = "true"
                    }
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(nickname.text.toString())
                        .build()
                    mFirebaseAuth!!.currentUser?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener(it) {
                                if(task.isSuccessful){
                                    findNavController().navigate(R.id.action_registrationFragment_to_homeFragment)
                                }else
                                    Toast.makeText(this.context,"Name update Failed, try again",Toast.LENGTH_LONG).show();
                            }
                        }
        }
        // [END create_user_with_email]

    }
    private fun updateUI(user: FirebaseUser?) {

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegistrationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                RegistrationFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}