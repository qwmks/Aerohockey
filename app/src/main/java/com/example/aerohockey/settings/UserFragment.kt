package com.example.aerohockey.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.aerohockey.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [userFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class userFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var userName : TextView
    lateinit var signOut:Button
    lateinit var updateName:Button
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
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
        auth= FirebaseAuth.getInstance()
        super.onViewCreated(view, savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(resources.getString(R.string.web_id))
                .requestEmail()
                .build()
        googleSignInClient = requireActivity().let { GoogleSignIn.getClient(it, gso) }
        userName=view.findViewById(R.id.nameEditText)
        signOut = view.findViewById(R.id.signOut)
        updateName = view.findViewById(R.id.updateName)
        userName.text=auth.currentUser?.displayName
        signOut.setOnClickListener {
            auth.signOut()
            googleSignInClient.signOut()
            findNavController().navigate(R.id.action_userFragment_to_mainActivity)
        }
        updateName.setOnClickListener {
            val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(userName.text.toString())
                    .build()
            auth.currentUser?.updateProfile(profileUpdates)?.addOnCompleteListener {task->
                if(task.isSuccessful){
                    Toast.makeText(this.context,"Complete!",Toast.LENGTH_LONG).show();
                }else
                    Toast.makeText(this.context,"Name update Failed, try again",Toast.LENGTH_LONG).show();
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment userFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            userFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}