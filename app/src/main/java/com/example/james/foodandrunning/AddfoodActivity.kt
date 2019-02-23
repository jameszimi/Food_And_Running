package com.example.james.foodandrunning

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_addfood.*




class AddfoodActivity : AppCompatActivity() {

    lateinit var unittype : Spinner
    private val TAG = "AddfoodActivity"
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addfood)


        val listOfType = arrayOf("กรัม", "มิลลิลิตร", "ซอง", "อัน", "ชิ้น")
        var type = 0

        unittype = findViewById(R.id.unittype_id)
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

        add_food.setOnClickListener {

            val predata = HashMap<String,Any>()
            val databarcode_id = barcode_id.text.toString()
            val dataserving_size = serving_size.text.toString()
            val dataenergy = energy.text.toString()
            val datafoodth = food_nameth.text.toString()
            val datafooden = food_nameen.text.toString()


            if (databarcode_id.isEmpty()) {
                predata["barcode_id"] = 0
            } else {
                predata["barcode_id"] = databarcode_id.toInt()
            }

            if (dataserving_size.isEmpty()) {
                predata["serving_size"] = 0
                Toast.makeText(this, "กรุณาใส่ serving size", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                predata["serving_size"] = dataserving_size.toInt()
            }

            if (dataenergy.isEmpty()) {
                predata["energy"] = 0
                Toast.makeText(this, "กรุณาใส่ Energy", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                predata["energy"] = dataenergy.toInt()
            }

            if (datafoodth.isEmpty()) {
                Toast.makeText(this, "กรุณาใส่ Food name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                predata["food_nameth"] = datafoodth
            }

            if (datafooden.isEmpty()) {
                predata["food_nameen"] = ""
            } else {
                predata["food_nameen"] = datafooden
            }

            predata["unit_id"] = type
            predata["food_update"] = FieldValue.serverTimestamp()
            predata["updateby"] = "James"


            validateData(predata)
            /*db.collection("FOOD_TABLE").add(predata).addOnSuccessListener {
                db.collection("FOOD_TABLE").document(it.id).set(predata)
           }*/


        }





    }

    private fun validateData(predata: HashMap<String, Any>) {

        val namefood = food_nameth.text.toString()
        val foodGetValidate = db.collection("FOOD_TABLE").whereEqualTo("food_nameth", namefood)

        foodGetValidate.get().addOnSuccessListener { doc ->
            if (doc.size() == 0) {
                saveDataFoodtoFB(predata)
            } else {
                Toast.makeText(this, "มี " + namefood + " อยู่ในฐานข้อมูลอยู่แล้ว", Toast.LENGTH_SHORT).show()
                return@addOnSuccessListener
            }

        }.addOnFailureListener {
            Log.d(TAG, "foodGetValidate Fail")
        }
    }

    private fun saveDataFoodtoFB(predata: HashMap<String, Any>) {

        val addDataFood = db.collection("FOOD_TABLE").document()

        predata["food_id"] = addDataFood.id

        addDataFood.set(predata).addOnSuccessListener {
            Toast.makeText(this, "บันทึกข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, AddfoodActivity::class.java))
        }.addOnFailureListener {
            Toast.makeText(this, "บันทึกข้อมูลไม่สำเร็จ", Toast.LENGTH_SHORT).show()
        }

    }


}


