package com.example.james.foodandrunning.firebase.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.james.foodandrunning.MainActivity
import com.example.james.foodandrunning.setupdata.AppPreferences
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

public class FirestoreFoodConsumeAuth(contextin: Context) {

    private val meal = (contextin as Activity).intent.getStringExtra("meal").toInt()
    private val uid = AppPreferences(contextin).getPreferenceUID()
    private var db = FirebaseFirestore.getInstance()
    private var queryAuth = db.collection("FOODCONSUME_TABLE").document()
    private val context = contextin

    fun UserCreateFood(userFoodName: String, userFoodCal: Int) {

        val foodid = "CreateByUser"
        val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val today = Date()
        val todayWithZeroTime = formatter.parse(formatter.format(today))
        val hashMap = HashMap<String,Any>()

        println("GGGGGGGGGGGGg : "+meal)
        hashMap["food_id"] = foodid
        hashMap["member_id"] = uid
        hashMap["repast_id"] = meal
        hashMap["calorie_total"] = userFoodCal
        hashMap["foodconsume_date"] = todayWithZeroTime
        hashMap["serving_size"] = 1

        queryAuth.set(hashMap).addOnSuccessListener {
            val intent = Intent(context,MainActivity::class.java)
            context.startActivity(intent)
            Toast.makeText(context,"SUCESSFULLY",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context,"Fail",Toast.LENGTH_SHORT).show()
        }
    }
}