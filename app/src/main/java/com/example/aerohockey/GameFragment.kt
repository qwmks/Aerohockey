package com.example.aerohockey

import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var gameView: GameView2? = null
    lateinit var stopEnemy:Button
    var isPaused = true
    var enIsPlaying = true
    lateinit var pauseButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameView = activity?.let { GameView2(it, Settings.field, Settings.puck, Settings.striker) }
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
        return inflater.inflate(R.layout.fragment_game, container, false)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layout: ConstraintLayout = view.findViewById(R.id.frameLayout)
        stopEnemy=view.findViewById(R.id.stopEnemy)
        //test

        layout.addView(gameView)
        pauseButton = view.findViewById(R.id.pauseButton)
        pauseButton.setOnClickListener {
            if (isPaused)
                gameView?.stop()
            else{
                gameView?.start()
            }
            isPaused=!isPaused
        }
        stopEnemy.setOnClickListener {

            if (enIsPlaying)
                gameView?.stopEnemy()
            else{
                gameView?.startEnemy()
            }
            enIsPlaying=!enIsPlaying
        }
    }

    override fun onResume() {
        super.onResume()
        gameView?.start()
    }

    override fun onPause() {
        super.onPause()
        gameView?.stop()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GameFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GameFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}