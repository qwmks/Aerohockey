package com.example.aerohockey

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GameOverFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameOverFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private val  email= FirebaseAuth.getInstance().currentUser?.email
    private var param2: String? = null
    lateinit var goToHome: Button
    lateinit var scoreView: TextView
    lateinit var loadingCircleGO: CircularProgressIndicator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: GameOverFragmentArgs by navArgs()
        goToHome = view.findViewById(R.id.goToHome)
        scoreView = view.findViewById(R.id.scoreView)
        loadingCircleGO = view.findViewById(R.id.loadingCircleGO)
        loadingCircleGO.visibility=View.VISIBLE
        scoreView.text= args.score.toString()
        goToHome.visibility=View.GONE
        DBHelper.addMoney(email,args.score*100,::delay)
        goToHome.setOnClickListener {
            findNavController().navigate(R.id.action_gameOverFragment_to_mainActivity2)
        }
    }
    private fun delay(res:Boolean){
        goToHome.visibility=View.VISIBLE
        loadingCircleGO.visibility=View.GONE
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_over, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GameOverFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GameOverFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}