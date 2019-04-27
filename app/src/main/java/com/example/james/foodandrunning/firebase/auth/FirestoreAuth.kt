package com.example.james.foodandrunning.firebase.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.james.foodandrunning.AddCalorieActivity
import com.example.james.foodandrunning.MainActivity
import com.example.james.foodandrunning.ResultsRunningActivity
import com.example.james.foodandrunning.RunningListActivity
import com.example.james.foodandrunning.setupdata.AppPreferences
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class FirestoreFoodConsumeAuth(contextin: Context) {

    private val meal = (contextin as Activity).intent.getStringExtra("meal").toInt()
    private val uid = AppPreferences(contextin).getPreferenceUID()
    private var db = FirebaseFirestore.getInstance()
    private var queryAuth = db.collection("FOODCONSUME_TABLE").document()
    private val context = contextin

    fun userCreateFood(userFoodName: String, userFoodCal: Int) {

        val foodid = "CreateByUser"
        val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val today = Date()
        val todayWithZeroTime = formatter.parse(formatter.format(today))
        val hashMap = HashMap<String,Any>()

        hashMap["food_id"] = foodid
        hashMap["member_id"] = uid
        hashMap["repast_id"] = meal
        hashMap["calorie_total"] = userFoodCal
        hashMap["foodconsume_date"] = todayWithZeroTime
        hashMap["serving_size"] = 1

        queryAuth.set(hashMap).addOnSuccessListener {
            val intent = Intent(context,MainActivity::class.java)
            (context as Activity).finish()
            context.startActivity(intent)
            Toast.makeText(context,"SUCESSFULLY",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context,"Fail",Toast.LENGTH_SHORT).show()
        }
    }

}

class FirestoreRunnigAuth(contextin: Context) {

    private val uid = AppPreferences(contextin).getPreferenceUID()
    private val weight = AppPreferences(contextin).getPreferenceWeight().toFloat()
    private val db = FirebaseFirestore.getInstance().collection("EXERCISE_TABLE")
    private var queryAuth = db.document()
    private val context = contextin

    fun addRunningManual(distance: Double, min: Double, calories: Double) {

        var distanceMiles = 0.00
        var mets = 0.0
        val hashDataRunning = HashMap<String,Any>()
        var totalCalories = 0.00

        var cheackcal = 0.00

        if (distance.toInt() != 0) distanceMiles = distance*0.000621371192 //metes to miles

        if (min.toInt() != 0 && distance.toInt() != 0 && calories.toInt() == 0){
            cheackcal = (distanceMiles/min) // mile per min
            val calinsixteenmin = cheackcal*60 // mile per hr

            when {
                calinsixteenmin < 4 -> mets = 5.0
                calinsixteenmin <= 5 -> mets = 8.3
                calinsixteenmin <= 5.2 -> mets = 9.0
                calinsixteenmin <= 6 -> mets = 9.8
                calinsixteenmin <= 6.7 -> mets = 10.5
                calinsixteenmin <= 7 -> mets = 11.0
                calinsixteenmin <= 7.5 -> mets = 11.5
                calinsixteenmin <= 8 -> mets = 11.8
                calinsixteenmin <= 8.6 -> mets = 12.3
                calinsixteenmin <= 9 -> mets = 12.8
                calinsixteenmin <= 10 -> mets = 14.5
                calinsixteenmin <= 11 -> mets = 16.0
                calinsixteenmin <= 12 -> mets = 19.0
                calinsixteenmin <= 13 -> mets = 19.8
                calinsixteenmin <= 14 -> mets = 23.0
                else -> Toast.makeText(context,"ผิดพลาด",Toast.LENGTH_SHORT).show()
            }

            //แคลลอรี (นาที) = (MET x น้ำหนัก (kg) x 3.5) / 200

            totalCalories = ((mets*weight*3.5)/200)*min
        }

        if (calories.toInt() != 0) {
            hashDataRunning["running_calorie"] = calories
        } else {

            hashDataRunning["member_id"] = uid
            if (distance.toInt() != 0) hashDataRunning["running_distance"] = distance
            hashDataRunning["running_calorie"] = totalCalories
            hashDataRunning["running_time"] = min
            hashDataRunning["running_date"] = FieldValue.serverTimestamp()
        }

        queryAuth.set(hashDataRunning).addOnSuccessListener {
            Toast.makeText(context, "เพิ่มรายการวิ่งสำเร็จ", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, MainActivity::class.java)
            (context as Activity).finish()
            context.startActivity(intent)
        }
            .addOnFailureListener {
                Toast.makeText(context, "เพิ่มรายการผิดพลาด", Toast.LENGTH_SHORT).show()
            }


    }

    fun runningPathtoFirestore(
        distancePath: ArrayList<LatLng>,
        showtime: String,
        calories: Double,
        sumdistance: Double
    ) {

        val distance = String.format("%.2f",sumdistance).toFloat()
        val hashDataRunning = HashMap<String,Any>()
        hashDataRunning["member_id"] = uid
        hashDataRunning["running_distance"] = distance
        hashDataRunning["running_calorie"] = String.format("%.2f",calories).toFloat()
        hashDataRunning["running_time"] = showtime
        hashDataRunning["running_route"] = distancePath
        hashDataRunning["running_date"] = FieldValue.serverTimestamp()

        queryAuth.set(hashDataRunning).addOnSuccessListener {
            val intent = Intent(context, ResultsRunningActivity::class.java)
            intent.putExtra("listmap",distancePath)
            intent.putExtra("stoptime",showtime)
            intent.putExtra("calories",calories.toString())
            intent.putExtra("distance",sumdistance.toString())
            context.startActivity(intent)
            (context as Activity).finish()
        }
            .addOnFailureListener {
                Toast.makeText(context, "กรุณาลแงใหม่อีกครั้ง", Toast.LENGTH_SHORT).show()
            }
    }

    fun deleateRunningPath(runningId: String) {
        db.document(runningId).delete().addOnSuccessListener {
            Toast.makeText(context,"ลบสำเร็จ",Toast.LENGTH_SHORT).show()
            val intent = Intent(context,RunningListActivity::class.java)
            context.startActivity(intent)
            (context as Activity).finish()
        }
            .addOnFailureListener {
                Toast.makeText(context,"ผิดพลาด ลบไม่สำเร็จ",Toast.LENGTH_SHORT).show()
            }
    }


}

class FirestoreFoodAuth(contextin: Context) {

    private val uid = AppPreferences(contextin).getPreferenceUID()
    private var queryAuth = FirebaseFirestore.getInstance().collection("FOOD_TABLE")
    private val context = contextin
    private val meal = (contextin as Activity).intent.getStringExtra("meals")

    fun searchFoodwithBarcode(contents: String) {

        var foodId : String = ""
        queryAuth.whereEqualTo("barcode_id",contents).get().addOnSuccessListener {

            if (it.isEmpty) Toast.makeText(context,"ไม่พบรายการที่ค้นหา", Toast.LENGTH_SHORT).show()
            if (it != null){
                for (doc in it) {
                    val dataHash = doc.data
                    foodId = dataHash["food_id"].toString()
                }

                val intent = Intent(context, AddCalorieActivity::class.java)
                intent.putExtra("food_id",foodId)
                intent.putExtra("meal", meal)
                context.startActivity(intent)
                (context as Activity).finish()
            }

        }.addOnFailureListener {
            Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show()
        }

    }
}