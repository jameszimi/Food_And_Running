package com.example.james.foodandrunning

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.james.foodandrunning.setupdata.AppPreferences
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_admineditfood.*

class AdmineditfoodActivity : AppCompatActivity() {

    lateinit var toolbar: ActionBar
    var db = FirebaseFirestore.getInstance()
    val TAG = "AdmineditfoodActivity "
    var type = 0
    lateinit var foodid : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admineditfood)

        setSupportActionBar(this.findViewById(R.id.mtoolbar))
        toolbar = supportActionBar!!
        toolbar.title = "แก้ไขข้อมูลอาหาร"
        toolbar.setDisplayHomeAsUpEnabled(true)

        var unit_id = 0
        foodid = intent.getStringExtra("food_id").toString()
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
            }
        }
            .addOnFailureListener {
                Toast.makeText(this,"ไม่พบรายการ",Toast.LENGTH_SHORT).show()
            }



        val listOfType = arrayOf("กรัม", "มิลลิลิตร", "ซอง", "อัน", "ชิ้น")

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

        adminupdate_food.setOnClickListener {
            validateName()
        }


    }
    private fun validateName() {
        val foodname = food_nameth.text.toString()
        val foodGetValidate = db.collection("FOOD_TABLE").whereEqualTo("food_nameth", foodname)

        foodGetValidate.get().addOnSuccessListener { doc ->
            for (data in doc) {
                println("james:${data.data}")
                Toast.makeText(this,"มีชื่ออาหารนี้อยู่ในฐานข้อมูลแล้ว",Toast.LENGTH_LONG).show()
                return@addOnSuccessListener
            }
            validateBarcode()

        }.addOnFailureListener {
            Log.d(TAG, "foodGetValidate Fail")
        }
    }

    private fun validateBarcode() {
        val barcode = barcode_id.text.toString().toInt()
        if (barcode == 0){
            updateFood()
        } else {
            val validateBarcode = db.collection("FOOD_TABLE").whereEqualTo("barcode_id",barcode)
            validateBarcode.get().addOnSuccessListener {
                for (data in it){
                    Toast.makeText(this,"มีบาร์โค้ดนี้อยู่ในระบบแล้ว",Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }
                updateFood()
            }
        }
    }

    private fun updateFood() {
        val predata = HashMap<String,Any>()
        predata["barcode_id"] = barcode_id.text.toString().toInt()
        predata["serving_size"] = serving_size.text.toString().toInt()
        predata["energy"] = energy.text.toString().toInt()
        predata["food_nameth"] = food_nameth.text.toString()
        predata["unit_id"] = type
        predata["food_update"] = FieldValue.serverTimestamp()
        predata["updateby"] = AppPreferences(this).getPreferenceUID()

        db.collection("FOOD_TABLE").document(foodid).update(predata).addOnSuccessListener {
            Toast.makeText(this,"อัพเดทข้อมูลสำเร็จ",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,FoodEditDetailActivity::class.java))
            this.finish()
        }.addOnFailureListener {
            Toast.makeText(this,"อัพเดทข้อมูลล้มเหลว",Toast.LENGTH_SHORT).show()
            return@addOnFailureListener
        }
    }
}
