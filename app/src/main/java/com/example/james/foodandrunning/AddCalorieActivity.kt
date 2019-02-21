package com.example.james.foodandrunning

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar

class AddCalorieActivity : AppCompatActivity() {

    lateinit var toolbar : ActionBar

    val TAG = "AddCalorieActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_calorie)

        val meal = intent.getStringExtra("meals").toInt()
        val food_nameth = intent.getStringExtra("food_nameth").toString()

        println(TAG+"bbbbbbbbbb"+meal+food_nameth)

        setSupportActionBar(findViewById(R.id.mtoolbar))
        toolbar = supportActionBar!!
        toolbar.title = "บันทึกแคลลอรี่"
        toolbar.setDisplayHomeAsUpEnabled(true)


    }
}
