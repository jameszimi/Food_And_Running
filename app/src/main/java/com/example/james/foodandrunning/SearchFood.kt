package com.example.james.foodandrunning

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore

import com.mancj.materialsearchbar.MaterialSearchBar

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
        val searchBar = findViewById<MaterialSearchBar>(R.id.searchView)
        searchBar.setHint("ค้นหา")
        searchBar.setSpeechMode(true)

        //database
       // val db = FirebaseFirestore.getInstance()
        //val foodGetData = db.collection("FOOD_TABLE")


        //set Adapter
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dataArray)
        listView.adapter = adapter

        //search action
        searchBar.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                //SEARCH FILTER
                println(TAG+charSequence)
                adapter.filter.filter(charSequence)

            }
            override fun afterTextChanged(editable: Editable) {
                //foodGetData.whereLessThanOrEqualTo("food_nameth", editable).get().addOnSuccessListener {
                 //   println(TAG + "Query by " + editable + it + "side " + it.size())
                //}
            }
        })

        //set on click
        listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(this@SearchFood, dataArray[i], Toast.LENGTH_SHORT).show() }

    }
}
