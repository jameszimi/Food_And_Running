package com.example.james.foodandrunning

import android.app.Dialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_userfoodcreat.*
import com.google.gson.Gson
import android.content.Context
import android.support.v7.app.ActionBar
import com.example.james.foodandrunning.setupdata.Userfood
import com.google.gson.reflect.TypeToken
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.example.james.foodandrunning.adapter.UserFoodAdapterList
import com.google.firebase.firestore.FirebaseFirestore


class UserfoodcreatActivity : AppCompatActivity() {

    var mExampleList : ArrayList<Userfood>? = null
    lateinit var mRecyclerView : RecyclerView
    lateinit var mLayoutManager : RecyclerView.LayoutManager
    lateinit var mAdapter : UserFoodAdapterList
    lateinit var toolbar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userfoodcreat)

        setSupportActionBar(findViewById(R.id.mtoolbar))
        toolbar = supportActionBar!!
        toolbar.title = "เพิ่มรายการอาหารที่ไม่มีอยู่ในรายการ"
        toolbar.setDisplayHomeAsUpEnabled(true)

        loadData()
        buildRecyclerView()

        val meal = intent.getStringExtra("meal")

        userfoodAddBtn.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_useraddfood)
            dialog.setTitle("เพิ่มเมนูที่ไม่มีอยู่ในระบบ")

            val cancleBtn = dialog.findViewById<Button>(R.id.cancleUseraddfood)
            val submitBtn = dialog.findViewById<Button>(R.id.submitUseraddfood)

            cancleBtn.setOnClickListener {
                dialog.dismiss()
            }

            submitBtn.setOnClickListener {
                var foodName = dialog.findViewById<EditText>(R.id.foodNameText).text.toString()
                val foodCal = dialog.findViewById<EditText>(R.id.foodCalText).text.toString()
                addfoodToFirebase(dialog,foodName,foodCal)
                userFoodaddDatatoFireStore(dialog,foodName,foodCal)
            }
            dialog.show()

        }



    }

    private fun userFoodaddDatatoFireStore(dialog: Dialog, foodName: String, foodCal: String) {
        val db = FirebaseFirestore.getInstance()
        val dbpartStore = db.collection("FOODCONSUME_TABLE").document()

    }

    private fun addfoodToFirebase(dialog: Dialog, foodNamein: String, foodCal: String) {
        var foodName = foodNamein
        if (foodName.isEmpty()){
            foodName = "ไม่มีชื่อ"
        }
        if (foodCal.isEmpty()) {
            Toast.makeText(this,"กรุณาระบุแคลลอรี่",Toast.LENGTH_SHORT).show()
        }

        println("addfoodToFirebase foodName:$foodName foodCal:$foodCal")
        mExampleList!!.add(Userfood(foodName,foodCal.toInt()))
        saveData()
        dialog.dismiss()


    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(mExampleList)
        editor.putString("userfoodcreate", json)
        editor.apply()
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("userfoodcreate", null)
        val type = object : TypeToken<ArrayList<Userfood>>() {}.type

        mExampleList = gson.fromJson(json, type)

        if (mExampleList == null){
            mExampleList = ArrayList()
        }

    }

    private fun buildRecyclerView() {
        mRecyclerView = findViewById(R.id.userfoodRecycleV)
        mRecyclerView.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(this)
        mAdapter = UserFoodAdapterList(this.mExampleList!!)

        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.adapter = mAdapter
    }

}
