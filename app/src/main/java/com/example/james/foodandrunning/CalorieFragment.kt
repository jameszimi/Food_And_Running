package com.example.james.foodandrunning


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_calorie.*
import kotlinx.android.synthetic.main.main_toolbar.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */private val TAG = "CalorieFragment "
class CalorieFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val v = inflater.inflate(R.layout.fragment_calorie, container, false)

        val addBF = v.findViewById<ImageView>(R.id.addBF)
        val addLunch = v.findViewById<ImageView>(R.id.addLunch)
        val addDinner = v.findViewById<ImageView>(R.id.addDinner)
        val addSnack = v.findViewById<ImageView>(R.id.addSnack)
        val calculateCalories = v.findViewById<Button>(R.id.calculateCal)

        //date
        val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val today = Date()
        val todayWithZeroTime = formatter.parse(formatter.format(today))

        val appPreferences = AppPreferences(v.context)
        val uid = appPreferences.getPreferenceUID()
        val db = FirebaseFirestore.getInstance()
        val getmeatpath = db.collection("FOODCONSUME_TABLE").whereEqualTo("foodconsume_date",todayWithZeroTime).whereEqualTo("member_id",uid)
        val getfoodname = db.collection("FOOD_TABLE")

        val arrayBFdata = mutableListOf<String>()
        getmeatpath.whereEqualTo("repast_id",1).get().addOnSuccessListener {bf ->
            //arrayBFdata.clear()
            var count = 0
            for (dbf in bf) {
                val dataHash = dbf.data
                println(TAG+"dataHash "+dataHash)
                getfoodname.document(dataHash["food_id"].toString()).get().addOnSuccessListener {
                    println(TAG+"getfoodneme "+it.data)
                }.addOnFailureListener {
                    Log.d(TAG,"getfoodnamedata Fail")
                }
            }
        }




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
