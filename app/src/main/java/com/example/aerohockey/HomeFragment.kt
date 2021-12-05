package com.example.aerohockey

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var text: TextView
    lateinit var signOut: Button
    lateinit var settingsBut: Button
    lateinit var playBut: Button
    lateinit var addMoneyBut: Button
    lateinit var moneyTextView: TextView
    lateinit var pucksTextView: TextView
    lateinit var strikerChange:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getResources().getString(R.string.web_id))
            .requestEmail()
            .build()
        googleSignInClient = requireActivity().let { GoogleSignIn.getClient(it, gso) }
        signOut = view.findViewById(R.id.sign_out)!!
        if (auth.currentUser ==null){
            findNavController().navigate(R.id.action_global_signinFragment)
        }
        playBut = view.findViewById(R.id.butPlay)
        playBut.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_gameActivity)
        }
        signOut.setOnClickListener {
            auth.signOut()
            googleSignInClient.signOut()
            findNavController().navigate(R.id.action_global_signinFragment)
        }
        DBHelper.getSettings(auth.currentUser?.email,::waitTillLoad)

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.


        pucksTextView = view.findViewById(R.id.Pucks)
        var puckString:String = ""
        Settings.unlockedPucks.forEach{
            "$puckString $it"
        }
        pucksTextView.text = Settings.unlockedPucks.toString()
//        pucksTextView.text=Settings.field.toString()
        addMoneyBut = view.findViewById(R.id.addMoneyButton)
        addMoneyBut.setOnClickListener {
            Log.d("Current money",Settings.money.value.toString())
            DBHelper.addMoney(auth.currentUser?.email, 100 )
        }
        settingsBut = view.findViewById(R.id.settingsBut)
        settingsBut.setOnClickListener {
//            findNavController().navigate(R.id.action_homeFragment_to_settingsActivity)
        }
        strikerChange = view.findViewById(R.id.strikerChange)
        strikerChange.setOnClickListener {
            if (Settings.striker==0)
                Settings.striker=1
            else
                Settings.striker=0
//            Toast.makeText(this.context,Settings.striker.toString(),Toast.LENGTH_SHORT).show()
        }

    }
    private fun waitTillLoad(result:Boolean){
        if (result){
            Toast.makeText(this.context,"All loaded",Toast.LENGTH_LONG).show()
        }
    }
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser: FirebaseUser? = auth.getCurrentUser()
        updateUI(currentUser)
    }
    private fun updateUI(user: FirebaseUser?) {
        text = view?.findViewById(R.id.welcomeText)!!
        if (user != null) {
            text.setText(user.displayName)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val i = inflater.inflate(R.layout.fragment_home, container, false)
        moneyTextView = i.findViewById(R.id.money)
        moneyTextView.text=Settings.money.toString()
        val moneyObserver = Observer<Int> { newValue ->
            // Update the UI, in this case, a TextView. {
            moneyTextView.text = newValue.toString()
        }
        Settings.money.observe(viewLifecycleOwner, moneyObserver)
        return i
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}