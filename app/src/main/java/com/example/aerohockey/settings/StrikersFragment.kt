package com.example.aerohockey.settings

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.aerohockey.DBHelper
import com.example.aerohockey.Prices
import com.example.aerohockey.R
import com.example.aerohockey.Settings
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StrikersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StrikersFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var auth= FirebaseAuth.getInstance()
    private var butsEnabled:MutableList<Boolean> = mutableListOf(true,true,true,true)
    lateinit var selectFirstButton: Button
    lateinit var selectSecondButton: Button
    lateinit var selectThirdButton: Button
    lateinit var selectDefaultButton: Button
    lateinit var buyFirstButton: Button
    lateinit var buySecondButton: Button
    lateinit var buyThirdButton: Button
    lateinit var loadingCircle: CircularProgressIndicator
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
        return inflater.inflate(R.layout.fragment_strikers, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email =auth.currentUser?.email.toString()
        selectFirstButton = view.findViewById(R.id.selectFirstStriker)
        selectSecondButton = view.findViewById(R.id.selectSecondStriker)
        selectThirdButton = view.findViewById(R.id.selectThirdStriker)
        selectDefaultButton = view.findViewById(R.id.selectDefaultStriker)
        buyFirstButton=view.findViewById(R.id.buyFirstStriker)
        buySecondButton=view.findViewById(R.id.buySecondStriker)
        buyThirdButton=view.findViewById(R.id.buyThirdStriker)
        loadingCircle = view.findViewById(R.id.loadingCircleStriker)

        when(Settings.striker){
            0->selectDefaultButton.text=getString(R.string.curr_selected)
            1->selectFirstButton.text=getString(R.string.curr_selected)
            2->selectSecondButton.text=getString(R.string.curr_selected)
            3->selectThirdButton.text=getString(R.string.curr_selected)
        }
        selectDefaultButton.setOnClickListener {
            Settings.striker=0
            DBHelper.saveSettings(email)
            hideButton(selectDefaultButton)
        }
        selectFirstButton.setOnClickListener {
            Settings.striker=1
            DBHelper.saveSettings(email)
            hideButton(selectFirstButton)
        }
        selectSecondButton.setOnClickListener {
            Settings.striker=2
            DBHelper.saveSettings(email)
            hideButton(selectSecondButton)
        }
        selectThirdButton.setOnClickListener {
            Settings.striker=3
            DBHelper.saveSettings(email)
            hideButton(selectThirdButton)
        }
        checkButtons(true)
        buyFirstButton.setOnClickListener {
            if (butsEnabled[1]) {
                loadingCircle.visibility=View.VISIBLE
                DBHelper.makePurchase(email, Prices.strikerPrices[1], 3, 1){
                    res->checkButtons(res)
                }
            } else
                Snackbar.make(view,R.string.no_money, Snackbar.LENGTH_LONG).show()
//            checkButtons()
        }
        buySecondButton.setOnClickListener {
            if (butsEnabled[2]) {
                loadingCircle.visibility=View.VISIBLE
                DBHelper.makePurchase(email, Prices.strikerPrices[2], 3, 2){
                    res->checkButtons(res)
                }
            } else
                Snackbar.make(view,R.string.no_money, Snackbar.LENGTH_LONG).show()
//            checkButtons(true)
        }
        buyThirdButton.setOnClickListener {
            if (butsEnabled[3]) {
                loadingCircle.visibility=View.VISIBLE
                DBHelper.makePurchase(email, Prices.strikerPrices[3], 3, 3){
                    res->checkButtons(res)
                }
            } else
                Snackbar.make(view,R.string.no_money, Snackbar.LENGTH_LONG).show()
//            checkButtons()
        }
//        Log.d("Settings.unlockedFields",Settings.unlockedFields.toString())
////        Log.d("Settings.unlockedFields[1]", Settings.unlockedFields[1].toString())
//        Log.d("1 in ",(1 in Settings.unlockedFields).toString())
//        Log.d("one in ",(1 in Settings.unlockedFields).toString())
//        Log.d("Contains 1",Settings.unlockedFields.contains(1).toString())
//        Log.d("using any",Settings.unlockedFields.any{ it == 1 }.toString())
    }
    private fun checkButtons(res:Boolean){
        if (res) {
            Log.d("Post-transaction", Settings.unlockedFields.toString())
            if (!(Settings.unlockedStrikers.any { it == 1 })) {
                selectFirstButton.visibility = View.GONE
            }
            if (!(Settings.unlockedStrikers.any { it == 2 })) {
                selectSecondButton.visibility = View.GONE
            }
            if (!(Settings.unlockedStrikers.any { it == 3 })) {
                selectThirdButton.visibility = View.GONE
            }
            if ((Settings.unlockedStrikers.any { it == 1 })) {
                buyFirstButton.visibility = View.GONE
            } else if (Prices.strikerPrices[1] > Settings.money.value!!) {
                buyFirstButton.setTextColor(Color.RED)
                butsEnabled[1] = false
            }
            if ((Settings.unlockedStrikers.any { it == 2 })) {
                buySecondButton.visibility = View.GONE
            } else if (Prices.strikerPrices[2] > Settings.money.value!!) {
                buySecondButton.setTextColor(Color.RED)
                butsEnabled[2] = false
            }
            if (Settings.unlockedStrikers.any{ it == 1 })
                selectFirstButton.visibility=View.VISIBLE
            if (Settings.unlockedStrikers.any{ it == 2 })
                selectSecondButton.visibility=View.VISIBLE
            if (Settings.unlockedStrikers.any{ it == 3 })
                selectThirdButton.visibility=View.VISIBLE
            if ((Settings.unlockedStrikers.any { it == 3 })) {
                buyThirdButton.visibility = View.GONE
            } else if (Prices.strikerPrices[3] > Settings.money.value!!) {
                buyThirdButton.setTextColor(Color.RED)
                butsEnabled[3] = false
            }
            loadingCircle.visibility=View.GONE
        } else view?.let { Snackbar.make(it,R.string.no_money, Snackbar.LENGTH_LONG).show()
            loadingCircle.visibility=View.GONE}
    }
    fun hideButton(button:Button){
        selectDefaultButton.visibility=View.VISIBLE
        if (Settings.unlockedStrikers.any{ it == 1 })
            selectFirstButton.visibility=View.VISIBLE
        if (Settings.unlockedStrikers.any{ it == 2 })
            selectSecondButton.visibility=View.VISIBLE
        if (Settings.unlockedStrikers.any{ it == 3 })
            selectThirdButton.visibility=View.VISIBLE
//        button.visibility=View.GONE
        selectDefaultButton.text= getString(R.string.select_default)
        selectFirstButton.text= getString(R.string.select_first)
        selectSecondButton.text= getString(R.string.select_second)
        selectThirdButton.text= getString(R.string.select_third)
        button.text=getString(R.string.curr_selected)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StrikersFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StrikersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}