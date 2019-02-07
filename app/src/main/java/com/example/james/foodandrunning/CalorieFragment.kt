package com.example.james.foodandrunning


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_calorie.*
import kotlinx.android.synthetic.main.main_toolbar.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class CalorieFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val v = inflater.inflate(R.layout.fragment_calorie, container, false)

        val addCal : Int
        val addBF = v.findViewById<ImageView>(R.id.addBF)
        val addLunch = v.findViewById<ImageView>(R.id.addLunch)
        val addDinner = v.findViewById<ImageView>(R.id.addDinner)
        val addSnack = v.findViewById<ImageView>(R.id.addSnack)
        val calculateCalories = v.findViewById<Button>(R.id.calculateCal)



        addBF.setOnClickListener {
            val clickIntent = Intent(activity,SearchFood::class.java)
            clickIntent.putExtra("meals","1")
            startActivity(clickIntent)
        }

        addLunch.setOnClickListener {
            val clickIntent = Intent(activity,SearchFood::class.java)
            clickIntent.putExtra("meals","2")
            startActivity(clickIntent)
        }

        addDinner.setOnClickListener {
            val clickIntent = Intent(activity,SearchFood::class.java)
            clickIntent.putExtra("meals","3")
            startActivity(clickIntent)
        }

        addSnack.setOnClickListener {
            val clickIntent = Intent(activity,SearchFood::class.java)
            clickIntent.putExtra("meals","4")
            startActivity(clickIntent)
        }

        return v
    }

 //

    companion object {
        fun newInstance(): CalorieFragment = CalorieFragment()
    }


}
