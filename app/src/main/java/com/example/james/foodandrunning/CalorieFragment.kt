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
import com.example.james.foodandrunning.setupdata.AppPreferences
import com.example.james.foodandrunning.setupdata.FoodNCal
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.lang.ArithmeticException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


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
        val reportCal = v.findViewById<Button>(R.id.calculateCal)
        //val calculateCalories = v.findViewById<Button>(R.id.calculateCal)

        //date
        val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val today = Date()
        val todayWithZeroTime = formatter.parse(formatter.format(today))
        println(TAG+"date "+todayWithZeroTime.toString())

        val appPreferences = AppPreferences(v.context)
        val uid = appPreferences.getPreferenceUID()
        val db = FirebaseFirestore.getInstance()
        val getmeatpath = db.collection("FOODCONSUME_TABLE").whereEqualTo("foodconsume_date",todayWithZeroTime).whereEqualTo("member_id",uid)
        val getfoodname = db.collection("FOOD_TABLE")
        val age = appPreferences.getPreferenceAge()
        val count = appPreferences.getPreferenceCount()
        val sex = appPreferences.getPreferenceSex()
        val weight = appPreferences.getPreferenceWeight()
        val height = appPreferences.getPreferenceHeight()
        val status = appPreferences.getPreferenceStatus()
        val routine = appPreferences.getPreferenceRoutine()
        val calories = appPreferences.getPreferenceCal()
        appPreferences.setPreferenceCount(count+1)
        if (count > 5) {
            appPreferences.setPreferenceCount(0)
            getuserData(uid)
        }

        if (calories == 0 && sex != 0 && weight != 0 && routine != 0 && age != 0) {
           sumCalCalories(sex, weight, routine, age)
        }
        println(TAG+"age:$age sex:$sex weight:$weight height:$height status:$status routine:$routine")

        //show call
        val calperday = v.findViewById<TextView>(R.id.calperday)
        calperday.text = calories.toString() + " Cal"


        //adapter






        //Get morning List





        try { 
            
            getBF(v,getmeatpath,getfoodname)

        } catch (e: ArithmeticException) {
            Log.d(TAG,"Fail to get data")
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

        reportCal.setOnClickListener {
            val bf = appPreferences.getPreferenceBFCal()
            val lu = appPreferences.getPreferenceLUCal()
            val dn = appPreferences.getPreferenceDNCal()
            val sn = appPreferences.getPreferenceSNCal()
            val calall = appPreferences.getPreferenceTotalEat()
            val clickIntent = Intent(activity,CaloriesReport::class.java)
            clickIntent.putExtra("bf",bf.toString())
            clickIntent.putExtra("lu",lu.toString())
            clickIntent.putExtra("din",dn.toString())
            clickIntent.putExtra("sn",sn.toString())
            clickIntent.putExtra("totalCal",calall.toString())
            startActivity(clickIntent)
        }

        return v
    }

    private fun getBF(
        v: View,
        getmeatpath: Query,
        getfoodname: CollectionReference
    ) {
        val arrayBFName = mutableListOf<String>()
        val arrayBFCal = mutableListOf<Int>()
        val bfList = ArrayList<FoodNCal>()
        var bfTotal : Int
        val totalBF = v.findViewById<TextView>(R.id.totalBF)
        val listbreakfast = v.findViewById(R.id.listbreakfast) as RecyclerView
        listbreakfast.layoutManager = LinearLayoutManager(activity)

        getmeatpath.whereEqualTo("repast_id",1).get().addOnSuccessListener {bf ->
            //arrayBFdata.clear()
            bfTotal = 0
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

            AppPreferences(this.context!!).setPreferenceBFCal(bfTotal)
            val foodtotal = bfTotal
            AppPreferences(this.context!!).setPreferenceTotalEat(foodtotal)
            getLU(v,getmeatpath,getfoodname,foodtotal)

        }.addOnFailureListener {
            Log.d(TAG,"List Food Name get Fail")
        }
    }

    private fun getLU(
        v: View,
        getmeatpath: Query,
        getfoodname: CollectionReference,
        bfTotal: Int
    ) {

        //Get Lunch List
        val arrayLuName = mutableListOf<String>()
        val arrayLuCal = mutableListOf<Int>()
        val luList = ArrayList<FoodNCal>()
        var luTotal : Int
        val totalLu = v.findViewById<TextView>(R.id.totalLunch)
        val listLunch = v.findViewById(R.id.listoflunch) as RecyclerView
        listLunch.layoutManager = LinearLayoutManager(activity)

        getmeatpath.whereEqualTo("repast_id",2).get().addOnSuccessListener {lu ->
            //arrayLudata.clear()
            luTotal = 0
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

            AppPreferences(this.context!!).setPreferenceLUCal(luTotal)
            val foodtotal = bfTotal+luTotal
            getDN(v,getmeatpath,getfoodname,foodtotal)


        }.addOnFailureListener {
            Log.d(TAG,"List Food Name get Fail")
        }

    }

    private fun getDN(v: View, getmeatpath: Query, getfoodname: CollectionReference, foodtotalin: Int) {

        //Get Dinner List
        val arrayDinName = mutableListOf<String>()
        val arrayDinCal = mutableListOf<Int>()
        val dinList = ArrayList<FoodNCal>()
        var dinTotal : Int
        val totalDin = v.findViewById<TextView>(R.id.totalDinner)
        val listDin = v.findViewById(R.id.listofDinner) as RecyclerView
        listDin.layoutManager = LinearLayoutManager(activity)

        getmeatpath.whereEqualTo("repast_id",3).get().addOnSuccessListener {lu ->
            //arrayLudata.clear()
            dinTotal = 0
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

            AppPreferences(this.context!!).setPreferenceDNCal(dinTotal)
            val foodtotal = foodtotalin+dinTotal
            getSN(v,getfoodname,getmeatpath,foodtotal)

        }.addOnFailureListener {
            Log.d(TAG,"List Food Name get Fail")
        }


    }

    private fun getSN(v: View, getfoodname: CollectionReference, getmeatpath: Query, foodtotalin : Int) {

        //Get Snack List
        val arraySNName = mutableListOf<String>()
        val arraySNCal = mutableListOf<Int>()
        val sNList = ArrayList<FoodNCal>()
        var sNTotal : Int
        val totalSN = v.findViewById<TextView>(R.id.totalSnack)
        val listSn = v.findViewById(R.id.listofSnack) as RecyclerView
        listSn.layoutManager = LinearLayoutManager(activity)

        getmeatpath.whereEqualTo("repast_id",4).get().addOnSuccessListener {lu ->
            //arrayLudata.clear()
            sNTotal = 0
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

                        println(TAG+"arrayBFName: "+arraySNName+" arraySnCal "+arraySNName)
                        println(TAG+"fffffffff"+sNList)
                        listSn.adapter = BreakfastList(sNList)

                    }
                }.addOnFailureListener {
                    Log.d(TAG,"getfoodnamedata Fail")
                }

                totalSN.text = sNTotal.toString()
            }

            AppPreferences(this.context!!).setPreferenceSNCal(sNTotal)
            val foodtotal = foodtotalin+sNTotal
            setTotalCal(foodtotal,v)

        }.addOnFailureListener {
            Log.d(TAG,"List Food Name get Fail")
        }

    }

    private fun setTotalCal(calAllTotal: Int, v: View) {
        val calAllTotalText = v.findViewById<TextView>(R.id.calAllTotal)
        calAllTotalText.text = calAllTotal.toString()+" Cal"
    }

    private fun sumCalCalories(
        sex: Int,
        weight: Int,
        routinein: Int,
        age: Int
    ) {

        //TEE = REE x AF
        val routine = when (routinein) {
            1 -> 1.2
            2 -> 1.45
            3 -> 1.65
            4 -> 1.85
            else -> 2.2
        }
        if (sex == 1) {
            var calories:Double = 0.00
            if (age <= 3) calories = ((60.9*weight)-54)*routine
            if (age <= 10) calories = ((22.7*weight)-495)*routine
            if (age <= 18) calories = ((17.5*weight)+651)*routine
            if (age <= 30) calories = ((15.3*weight)+679)*routine
            if (age <= 60) calories = ((11.2*weight)+879)*routine
            else calories = ((13.5*weight)+987)*routine

            println(TAG+" calories:$calories")
            AppPreferences(this.context!!).setPreferenceCal(calories.toInt())

        } else {
            var calories:Double = 0.00
            if (age <= 3) calories = ((61.0*weight)-51)*routine
            if (age <= 10) calories = ((22.5*weight)+499)*routine
            if (age <= 18) calories = ((12.2*weight)+746)*routine
            if (age <= 30) calories = ((14.7*weight)+996)*routine
            if (age <= 60) calories = ((8.7*weight)+829)*routine
            else calories = ((10.5*weight)+596)*routine

            println(TAG+" calories:$calories")
            AppPreferences(this.context!!).setPreferenceCal(calories.toInt())
        }
    }

    private fun getuserData(uid: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("MEMBER_TABLE").document(uid).get().addOnSuccessListener {
            if (it != null) {
                val dataHash = it.data as HashMap<String, Any>
                val name = dataHash["member_name"].toString()
                val password = dataHash["member_password"].toString()
                val email = dataHash["member_email"].toString()
                val age = dataHash["member_birthday"].toString()
                val sex = dataHash["member_sex"].toString().toInt()
                val weight = dataHash["member_weight"].toString()
                val height = dataHash["member_height"].toString().toInt()
                val status = dataHash["member_status"].toString().toInt()
                val routine = dataHash["member_diaryroutine"].toString().toInt()
                validate(age, sex, height, status, routine, name, password, email)
                getWeight(weight)
            }

        }.addOnFailureListener {
            Log.d(TAG, "getuserData Fail")
        }


    }

    private fun getWeight(weightpath: String) {

        FirebaseFirestore.getInstance().collection("WEIGHT_TABLE").document(weightpath).get().addOnSuccessListener {
            if (it != null) {
                val dataHash = it.data as HashMap<String, Any>
                val weight = dataHash["weight_value"].toString().toInt()
                AppPreferences(this.context!!).setPreferenceWeight(weight)
                println(TAG+" weight_value $weight")
            }
        }

    }

    private fun validate(
        age: String,
        sex: Int,
        height: Int,
        status: Int,
        routine: Int,
        name: String,
        password: String,
        email: String
    ) {
        val datecount = age.length
        var yyyy = 0
        val yearnow = 2562
        if (datecount == 6) yyyy = age.substring(2).toInt()
        if (datecount == 7) yyyy = age.substring(3).toInt()
        if (datecount == 8) yyyy = age.substring(4).toInt()

        val appPreferences = AppPreferences(this.context!!)
        appPreferences.setPreferenceAge(yearnow-yyyy)
        appPreferences.setPreferenceSex(sex)
        appPreferences.setPreferenceHeight(height)
        appPreferences.setPreferenceStatus(status)
        appPreferences.setPreferenceRoutine(routine)
        appPreferences.setPreferenceUsername(name)
        appPreferences.setPreferencePassword(password)
        appPreferences.setPreferenceEmail(email)
        appPreferences.setPreferenceBirthday(age.toInt())
        println(TAG+"yyyy"+(yearnow-yyyy)+" sex $sex height $height status $status routine $routine")
    }

    //

    companion object {
        fun newInstance(): CalorieFragment = CalorieFragment()
    }

}


