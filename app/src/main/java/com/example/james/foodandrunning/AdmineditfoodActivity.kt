package com.example.james.foodandrunning

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.james.foodandrunning.setupdata.FoodDetial
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_admineditfood.*

class AdmineditfoodActivity : AppCompatActivity() {

    lateinit var toolbar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admineditfood)

        setSupportActionBar(this.findViewById(R.id.mtoolbar))
        toolbar = supportActionBar!!
        toolbar.title = "แก้ไขข้อมูลอาหาร"
        toolbar.setDisplayHomeAsUpEnabled(true)

        var unit_id = 0
        val foodid = intent.getStringExtra("food_id").toString()
        println("food_id:$foodid")
        FirebaseFirestore.getInstance().collection("FOOD_TABLE").document(foodid).get().addOnSuccessListener {
            if (it.data != null){
                val dataHash = it.data
                println("datahash:$dataHash")
                barcode_id.setText(dataHash!!["barcode_id"].toString())
                food_nameth.setText(dataHash!!["food_nameth"].toString())
                serving_size.setText(dataHash!!["serving_size"].toString())
                energy.setText(dataHash!!["energy"].toString())
                unit_id = dataHash!!["unit_id"].toString().toInt()
                food_nameen.setText(dataHash!!["food_nameen"].toString())
            }
        }
            .addOnFailureListener {
                Toast.makeText(this,"ไม่พบรายการ",Toast.LENGTH_SHORT).show()
            }



        val listOfType = arrayOf("กรัม", "มิลลิลิตร", "ซอง", "อัน", "ชิ้น")
        var type = 0

        val unittype = findViewById<Spinner>(R.id.unittype_id)
        unittype.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,listOfType)


        unittype.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val types = listOfType[position].trim()
                type = when (types) {
                    "กรัม" -> 1
                    "มิลลิลิตร" -> 2
                    "ซอง" -> 3
                    "อัน" -> 4
                    "ชิ้น" -> 5
                    else -> 6
                }
            }

        }


    }
}
