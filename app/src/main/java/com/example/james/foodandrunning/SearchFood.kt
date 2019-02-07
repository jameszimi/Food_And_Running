package com.example.james.foodandrunning

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.widget.Toolbar

class SearchFood : AppCompatActivity() {

    lateinit var mtoolbar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchfood)

        setSupportActionBar(findViewById(R.id.mtoolbar))
        mtoolbar = supportActionBar!!
        mtoolbar.title = "ค้นหารายการอาหาร"
        mtoolbar.setDisplayHomeAsUpEnabled(true)

        val meal = intent.getStringExtra("meals").toInt()

    }
}
