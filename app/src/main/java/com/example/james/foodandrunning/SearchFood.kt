package com.example.james.foodandrunning

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import com.example.james.foodandrunning.adapter.SearchFoodAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_searchfood.*
import android.widget.Toast
import com.example.james.foodandrunning.firebase.auth.FirestoreFoodAuth
import kotlinx.android.synthetic.main.activity_addfood.*


class SearchFood : AppCompatActivity() {

    lateinit var toolbar: ActionBar
    val TAG = "SearchFood "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchfood)

        setSupportActionBar(findViewById(R.id.mtoolbar))
        toolbar = supportActionBar!!
        toolbar.title = "ค้นหารายการอาหาร"
        toolbar.setDisplayHomeAsUpEnabled(true)

        val meal = intent.getStringExtra("meals")
        val searchBar = findViewById<SearchView>(R.id.searchView)


        //database
        val db = FirebaseFirestore.getInstance()
        val foodGetData = db.collection("FOOD_TABLE")


        //test adapter
        recyclerView_searchfood.layoutManager = LinearLayoutManager(this)





        //set Adapter

        val arrayofData = mutableSetOf<String>()
        //search action
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    callData(query)
                } else {
                    arrayofData.clear()
                    recyclerView_searchfood.adapter = SearchFoodAdapter(arrayofData, meal)
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    arrayofData.clear()
                    recyclerView_searchfood.adapter = SearchFoodAdapter(arrayofData, meal)
                } else {
                    callData(newText)
                }
                return true
            }

            fun callData(query: String) {
                arrayofData.clear()
                foodGetData.whereGreaterThanOrEqualTo("food_nameth",query).get().addOnSuccessListener { doc ->
                    for (document in doc) {
                        val dataHash = document.data
                        arrayofData.add(dataHash["food_nameth"].toString())
                        println(TAG + "aaaaaa data callData "+dataHash)
                    }

                    println("aaaaaaaaaa "+ arrayofData)
                    recyclerView_searchfood.adapter = SearchFoodAdapter(arrayofData, meal)
                    //adapterSetView(arrayofData,meal)
                }
            }

        })

        addusrfood_btnimg.setOnClickListener {
            val intent =Intent(this,UserfoodcreatActivity::class.java)
            intent.putExtra("meal",meal)
            startActivity(intent)
        }

        scanImgView.setOnClickListener {
            val scanner = IntentIntegrator(this)
            scanner.setOrientationLocked(false)
            scanner.setBeepEnabled(true)
            scanner.initiateScan()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
                } else {

                    FirestoreFoodAuth(this).searchFoodwithBarcode(result.contents)
                    Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }

            println("result : ${result.contents}")
        }
    }

}
