package com.example.james.foodandrunning

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.widget.SearchView
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_searchfood.*


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


        //set Adapter

        val arrayofData = mutableSetOf<String>()
        //search action
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    callData(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    listview_dynamic.adapter = null
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
                        println("aaaaaa"+dataHash)
                    }

                    println("aaaaaaaaaa "+ arrayofData)
                    adapterSetView(arrayofData,meal)
                }
            }

        })

    }

    private fun adapterSetView(arrayofData: MutableSet<String>, meal: String) {

        //val listView = findViewById<ListView>(R.id.listview_dynamic)
        val dataArray = arrayofData.toTypedArray()
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dataArray)
        listview_dynamic.adapter = adapter

        //set on click
        listview_dynamic.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->

            println("aaaaaaaaaa meal "+meal)
            val clickIntent = Intent(this,AddCalorieActivity::class.java)
            println(TAG+" meals "+meal)
            clickIntent.putExtra("meals",meal)
            clickIntent.putExtra("food_nameth",dataArray[i])
            startActivity(clickIntent)
            finish()

            Toast.makeText(this@SearchFood, dataArray[i], Toast.LENGTH_SHORT).show() }
        println("aAAAAAAAAA todo")

    }


}
