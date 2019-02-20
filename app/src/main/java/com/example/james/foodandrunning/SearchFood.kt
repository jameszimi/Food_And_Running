package com.example.james.foodandrunning

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.widget.SearchView
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore


class SearchFood : AppCompatActivity() {

    lateinit var mtoolbar: ActionBar
    val TAG = "SearchFood "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchfood)

        setSupportActionBar(findViewById(R.id.mtoolbar))
        //mtoolbar.setDisplayHomeAsUpEnabled(true)
       // mtoolbar = supportActionBar!!
       // mtoolbar.title = "ค้นหารายการอาหาร"


        var nameoffood= ""
        //datadummy
        val dataArray = arrayOf("ข้าวขาว", "ข้าวสวย", "มะนาว", "โซดา", "ละมุด", "ลำใย", "แตงโม", "ส้มโอ", "aaaa", "231")

        val meal = intent.getStringExtra("meals").toInt()
        val listView = findViewById<ListView>(R.id.listview_dynamic)
        val searchBar = findViewById<SearchView>(R.id.searchView)

        //database
        val db = FirebaseFirestore.getInstance()
        val foodGetData = db.collection("FOOD_TABLE")


        //set Adapter
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dataArray)
        listView.adapter = adapter

        var arrayofData: Set<String>
        //search action
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                callData(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                callData(newText)
                return true
            }

            fun callData(query: String) {
                foodGetData.whereGreaterThan("food_nameth",query).get().addOnSuccessListener { doc ->
                    var dataHash  = emptyMap<String,Any>()
                    for (document in doc) {
                         dataHash = document.data
                        arrayofData(dataHash["food_nameth"].toString())
                        println("aaaaaa"+dataHash)
                    }

                    println("aaaaaaaaaa "+ arrayofData)

                }
            }

        })






        //set on click
        listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(this@SearchFood, dataArray[i], Toast.LENGTH_SHORT).show() }

    }
}
