package com.example.james.foodandrunning

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_addfood.*




class AddfoodActivity : AppCompatActivity() {

    lateinit var unittype : Spinner
    lateinit var toolbar : ActionBar
    private val TAG = "AddfoodActivity"
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addfood)

        setSupportActionBar(this.findViewById(R.id.mtoolbar))
        toolbar = supportActionBar!!
        toolbar.title = "เพิ่มข้อมูลอาหาร"
        toolbar.setDisplayHomeAsUpEnabled(true)


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

        serving_size.setText("100")

        add_food.setOnClickListener {

            val predata = HashMap<String,Any>()
            val databarcode_id = barcode_id.text.toString()
            val dataserving_size = serving_size.text.toString()
            val dataenergy = energy.text.toString()
            val datafoodth = food_nameth.text.toString()


            if (databarcode_id.isEmpty()) {
                predata["barcode_id"] = 0
            } else {
                predata["barcode_id"] = databarcode_id
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



            predata["unit_id"] = type
            predata["food_update"] = FieldValue.serverTimestamp()
            predata["updateby"] = "James"


            validateData(predata)

        }

        addFoodScan.setOnClickListener {
            val scanner = IntentIntegrator(this)
            scanner.setOrientationLocked(false)
            scanner.setBeepEnabled(true)
            scanner.initiateScan()
        }




    }

    private fun validateData(predata: HashMap<String, Any>) {

        val namefood = food_nameth.text.toString()
        val foodGetValidate = db.collection("FOOD_TABLE").whereEqualTo("food_nameth", namefood)

        foodGetValidate.get().addOnSuccessListener { doc ->
            for (data in doc) {
                println("james:${data.data}")
                Toast.makeText(this,"มีชื่ออาหารนี้อยู่ในฐานข้อมูลแล้ว",Toast.LENGTH_LONG).show()
                return@addOnSuccessListener
            }

            saveDataFoodtoFB(predata)

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
            this.finish()
        }.addOnFailureListener {
            Toast.makeText(this, "บันทึกข้อมูลไม่สำเร็จ", Toast.LENGTH_SHORT).show()
        }

    }

    override  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
                } else {

                    barcode_id.setText(result.contents)
                    Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }

            println("result : ${result.contents}")
        }
    }


}


