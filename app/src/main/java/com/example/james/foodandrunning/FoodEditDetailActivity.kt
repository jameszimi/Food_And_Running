package com.example.james.foodandrunning

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import com.example.james.foodandrunning.adapter.FoodNameAdapter
import com.example.james.foodandrunning.setupdata.FoodName
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_foodeditdetail.*

class FoodEditDetailActivity : AppCompatActivity() {

    lateinit var toolbar: ActionBar
    val foodNameList = ArrayList<FoodName>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foodeditdetail)

        //getFoodName()

        setSupportActionBar(findViewById(R.id.mtoolbar))
        toolbar = supportActionBar!!
        toolbar.title = "แก้ไขข้อมูลอาหาร"
        toolbar.setDisplayHomeAsUpEnabled(true)

        val db = FirebaseFirestore.getInstance().collection("FOOD_TABLE")

        val searchBar = findViewById<SearchView>(R.id.adminSearchView)
        adminRecyclerView_searchfood.layoutManager = LinearLayoutManager(this)

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0!!.isNotEmpty()){
                    getFoodName(p0)
                } else {
                    foodNameList.clear()
                    adminRecyclerView_searchfood.adapter = FoodNameAdapter(foodNameList)
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0!!.isEmpty()){
                    foodNameList.clear()
                    adminRecyclerView_searchfood.adapter = FoodNameAdapter(foodNameList)
                } else {
                    getFoodName(p0)
                }
                return true
            }

            fun getFoodName(p0: String) {
                foodNameList.clear()
                db.whereGreaterThanOrEqualTo("food_nameth",p0).get().addOnSuccessListener {
                        for (data in it){
                            val dataHash = data.data
                            foodNameList.add(FoodName(dataHash["food_nameth"].toString(),data.id))
                    }
                    adminRecyclerView_searchfood.adapter = FoodNameAdapter(foodNameList)
                }
            }

        })



    }


}
