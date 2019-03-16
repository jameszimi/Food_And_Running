package com.example.james.foodandrunning


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.james.foodandrunning.adapter.BreakfastList
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_calorie.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


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
        //val calculateCalories = v.findViewById<Button>(R.id.calculateCal)

        //date
        val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val today = Date()
        val todayWithZeroTime = formatter.parse(formatter.format(today))

        val appPreferences = AppPreferences(v.context)
        val uid = appPreferences.getPreferenceUID()
        val db = FirebaseFirestore.getInstance()
        val getmeatpath = db.collection("FOODCONSUME_TABLE").whereEqualTo("foodconsume_date",todayWithZeroTime).whereEqualTo("member_id",uid)
        val getfoodname = db.collection("FOOD_TABLE")

        //adapter
        val listbreakfast = v.findViewById(R.id.listbreakfast) as RecyclerView
        val listLunch = v.findViewById(R.id.listoflunch) as RecyclerView
        val listDin = v.findViewById(R.id.listofDinner) as RecyclerView
        val listSn = v.findViewById(R.id.listofSnack) as RecyclerView
        listbreakfast.layoutManager = LinearLayoutManager(activity)
        listLunch.layoutManager = LinearLayoutManager(activity)
        listDin.layoutManager = LinearLayoutManager(activity)
        listSn.layoutManager = LinearLayoutManager(activity)

        var calAllTotal = 0
        val calAllTotalText = v.findViewById<TextView>(R.id.calAllTotal)


        //Get morning List
        val arrayBFName = mutableListOf<String>()
        val arrayBFCal = mutableListOf<Int>()
        val bfList = ArrayList<FoodNCal>()
        var bfTotal = 0
        val totalBF = v.findViewById<TextView>(R.id.totalBF)

        getmeatpath.whereEqualTo("repast_id",1).get().addOnSuccessListener {bf ->
            //arrayBFdata.clear()

            for (dbf in bf) {
                val dataHash = dbf.data

                //put cal
                arrayBFCal.add(dataHash["calorie_total"].toString().toInt())
                bfTotal += dataHash["calorie_total"].toString().toInt()
                println(TAG+"dataHash:"+dataHash)

                //getName by FoodID
                getfoodname.document(dataHash["food_id"].toString()).get().addOnSuccessListener {
                    if (it != null) {
                        val foodName = it.data

                        bfList.add(FoodNCal(foodName!!["food_nameth"].toString(),dataHash["calorie_total"].toString().toInt(),dbf.id))

                        println(TAG+"foodName:"+foodName)
                        arrayBFName.add(foodName["food_nameth"].toString())

                        println(TAG+"foodname:" + foodName["food_nameth"])

                        println(TAG+"arrayBFName: "+arrayBFName+" arrayBFCal "+arrayBFCal)
                        listbreakfast.adapter = BreakfastList(bfList)

                    }
                }.addOnFailureListener {
                    Log.d(TAG,"getfoodnamedata Fail")
                }

                totalBF.text = bfTotal.toString()

            }
        }.addOnFailureListener {
            Log.d(TAG,"List Food Name get Fail")
        }

        //Get Lunch List
        val arrayLuName = mutableListOf<String>()
        val arrayLuCal = mutableListOf<Int>()
        val luList = ArrayList<FoodNCal>()
        var luTotal = 0
        val totalLu = v.findViewById<TextView>(R.id.totalLunch)

        getmeatpath.whereEqualTo("repast_id",2).get().addOnSuccessListener {lu ->
            //arrayLudata.clear()

            for (dlu in lu) {
                val dataHash = dlu.data

                //put cal
                arrayLuCal.add(dataHash["calorie_total"].toString().toInt())
                luTotal += dataHash["calorie_total"].toString().toInt()
                println(TAG+"dataHash:"+dataHash)

                //getName by FoodID
                getfoodname.document(dataHash["food_id"].toString()).get().addOnSuccessListener {
                    if (it != null) {
                        val foodName = it.data

                        luList.add(FoodNCal(foodName!!["food_nameth"].toString(),dataHash["calorie_total"].toString().toInt(),dlu.id))

                        println(TAG+"foodName:"+foodName)
                        arrayLuName.add(foodName["food_nameth"].toString())

                        println(TAG+"foodname:" + foodName["food_nameth"])

                        println(TAG+"arrayBFName: "+arrayLuName+" arrayBFCal "+arrayLuName)
                        listLunch.adapter = BreakfastList(luList)

                    }
                }.addOnFailureListener {
                    Log.d(TAG,"getfoodnamedata Fail")
                }

                totalLu.text = luTotal.toString()

            }
        }.addOnFailureListener {
            Log.d(TAG,"List Food Name get Fail")
        }

        //Get Dinner List
        val arrayDinName = mutableListOf<String>()
        val arrayDinCal = mutableListOf<Int>()
        val dinList = ArrayList<FoodNCal>()
        var dinTotal = 0
        val totalDin = v.findViewById<TextView>(R.id.totalDinner)

        getmeatpath.whereEqualTo("repast_id",3).get().addOnSuccessListener {lu ->
            //arrayLudata.clear()

            for (dlu in lu) {
                val dataHash = dlu.data

                //put cal
                arrayDinCal.add(dataHash["calorie_total"].toString().toInt())
                dinTotal += dataHash["calorie_total"].toString().toInt()
                println(TAG+"dataHash:"+dataHash)

                //getName by FoodID
                getfoodname.document(dataHash["food_id"].toString()).get().addOnSuccessListener {
                    if (it != null) {
                        val foodName = it.data

                        dinList.add(FoodNCal(foodName!!["food_nameth"].toString(),dataHash["calorie_total"].toString().toInt(),dlu.id))

                        println(TAG+"foodName:"+foodName)
                        arrayDinName.add(foodName["food_nameth"].toString())

                        println(TAG+"foodname:" + foodName["food_nameth"])

                        println(TAG+"arrayBFName: "+arrayDinName+" arrayBFCal "+arrayDinName)
                        listDin.adapter = BreakfastList(dinList)

                    }
                }.addOnFailureListener {
                    Log.d(TAG,"getfoodnamedata Fail")
                }

                totalDin.text = dinTotal.toString()

            }
        }.addOnFailureListener {
            Log.d(TAG,"List Food Name get Fail")
        }


        //Get Snack List
        val arraySNName = mutableListOf<String>()
        val arraySNCal = mutableListOf<Int>()
        val sNList = ArrayList<FoodNCal>()
        var sNTotal = 0
        val totalSN = v.findViewById<TextView>(R.id.totalSnack)

        getmeatpath.whereEqualTo("repast_id",4).get().addOnSuccessListener {lu ->
            //arrayLudata.clear()

            for (dlu in lu) {
                val dataHash = dlu.data

                //put cal
                arraySNCal.add(dataHash["calorie_total"].toString().toInt())
                sNTotal += dataHash["calorie_total"].toString().toInt()
                println(TAG+"dataHash:"+dataHash)

                //getName by FoodID
                getfoodname.document(dataHash["food_id"].toString()).get().addOnSuccessListener {
                    if (it != null) {
                        val foodName = it.data

                        sNList.add(FoodNCal(foodName!!["food_nameth"].toString(),dataHash["calorie_total"].toString().toInt(),dlu.id))

                        println(TAG+"foodName:"+foodName)
                        arraySNName.add(foodName["food_nameth"].toString())

                        println(TAG+"foodname:" + foodName["food_nameth"])

                        println(TAG+"arrayBFName: "+arraySNName+" arrayBFCal "+arraySNName)
                        listSn.adapter = BreakfastList(sNList)

                    }
                }.addOnFailureListener {
                    Log.d(TAG,"getfoodnamedata Fail")
                }

                totalSN.text = sNTotal.toString()
                calAllTotal = bfTotal+luTotal+dinTotal+sNTotal
                calAllTotalText.text = calAllTotal.toString()+" Cal"

            }
        }.addOnFailureListener {
            Log.d(TAG,"List Food Name get Fail")
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


    /*override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        menu!!.setHeaderTitle("เลือกรายการ")
        activity!!.menuInflater.inflate(R.menu.fooddetail_menu,menu)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        return super.onContextItemSelected(item)
    }*/
}

class FoodNCal(val foodName : String, val totalCal : Int, val foodConsume : String) {

}
