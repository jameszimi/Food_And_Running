package com.example.james.foodandrunning

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.james.foodandrunning.adapter.FoodNameAdapter
import com.example.james.foodandrunning.setupdata.FoodName
import com.google.firebase.firestore.FirebaseFirestore

class FoodEditDetailActivity : AppCompatActivity() {

    lateinit var toolbar: ActionBar
    val foodNameList = ArrayList<FoodName>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foodeditdetail)

        getFoodName()

        setSupportActionBar(findViewById(R.id.mtoolbar))
        toolbar = supportActionBar!!
        toolbar.title = "แก้ไขข้อมูลอาหาร"
        toolbar.setDisplayHomeAsUpEnabled(true)




    }

    private fun getFoodName() {
        FirebaseFirestore.getInstance().collection("FOOD_TABLE").get().addOnSuccessListener {
            if (it != null){
                for (data in it){
                    val dataHash = data.data
                    foodNameList.add(FoodName(dataHash["food_nameth"].toString(),data.id))
                }

                val foodnamelist = findViewById<RecyclerView>(R.id.foodeditrecycle)
                foodnamelist.layoutManager = LinearLayoutManager(this)
                foodnamelist.adapter = FoodNameAdapter(foodNameList)

            }
        }
    }
}
