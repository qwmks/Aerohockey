package com.example.aerohockey.settings

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
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fieldsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class fieldsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var auth=FirebaseAuth.getInstance()

    lateinit var selectFirstButton: Button
    lateinit var selectSecondButton: Button
    lateinit var selectThirdButton: Button
    lateinit var selectDefaultButton: Button
    lateinit var buyFirstButton: Button
    lateinit var buySecondButton: Button
    lateinit var buyThirdButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var email =auth.currentUser?.email.toString()
        selectFirstButton = view.findViewById(R.id.selectFirstField)
        selectSecondButton = view.findViewById(R.id.selectSecondField)
        selectThirdButton = view.findViewById(R.id.selectThirdField)
        selectDefaultButton = view.findViewById(R.id.selectDefaultField)
        buyFirstButton=view.findViewById(R.id.buyFirstField)
        buySecondButton=view.findViewById(R.id.buySecondField)
        buyThirdButton=view.findViewById(R.id.buyThirdField)
        when(Settings.field){
            0->selectDefaultButton.visibility=View.GONE
            1->selectFirstButton.visibility=View.GONE
            2->selectSecondButton.visibility=View.GONE
            3->selectThirdButton.visibility=View.GONE
        }
        selectDefaultButton.setOnClickListener {
            Settings.field=0
            DBHelper.saveSettings(email)
            hideButton(selectDefaultButton)
        }
        selectFirstButton.setOnClickListener {
            Settings.field=1
            DBHelper.saveSettings(email)
            hideButton(selectFirstButton)
        }
        selectSecondButton.setOnClickListener {
            Settings.field=2
            DBHelper.saveSettings(email)
            hideButton(selectSecondButton)
        }
        selectThirdButton.setOnClickListener {
            Settings.field=3
            DBHelper.saveSettings(email)
            hideButton(selectThirdButton)
        }
        if ((Settings.unlockedFields.any{ it == 1 }) or (Prices.fieldPrices[1]> Settings.money.value!!)){
            buyFirstButton.visibility=View.GONE
        }
        if ((Settings.unlockedFields.any{ it == 2 }) or (Prices.fieldPrices[2]> Settings.money.value!!)){
            buySecondButton.visibility=View.GONE
        }
        if ((Settings.unlockedFields.any{ it == 3 }) or (Prices.fieldPrices[3]> Settings.money.value!!)){
            buyThirdButton.visibility=View.GONE
        }
        buyFirstButton.setOnClickListener {
            DBHelper.makePurchase(email,Prices.fieldPrices[1],1,1)
            buyFirstButton.visibility=View.GONE
        }
        Log.d("Settings.unlockedFields",Settings.unlockedFields.toString())
//        Log.d("Settings.unlockedFields[1]", Settings.unlockedFields[1].toString())
        Log.d("1 in ",(1 in Settings.unlockedFields).toString())
        Log.d("one in ",(1 in Settings.unlockedFields).toString())
        Log.d("Contains 1",Settings.unlockedFields.contains(1).toString())
        Log.d("using any",Settings.unlockedFields.any{ it == 1 }.toString())
    }
    fun hideButton(button:Button){
        selectDefaultButton.visibility=View.VISIBLE
        selectFirstButton.visibility=View.VISIBLE
        selectSecondButton.visibility=View.VISIBLE
        selectThirdButton.visibility=View.VISIBLE
        button.visibility=View.GONE
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fields, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fieldsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fieldsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}