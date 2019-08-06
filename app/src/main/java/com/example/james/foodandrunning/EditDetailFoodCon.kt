package com.example.james.foodandrunning

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_editfoodcon.*

class EditDetailFoodCon : AppCompatActivity() {

    lateinit var toolbar : ActionBar

    private val TAG = "EditDetailFoodCon "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editfoodcon)

        val foodConsumeId = intent.getStringExtra("foodConsumeId").toString()
        println(TAG+foodConsumeId)
        val foodName = intent.getStringExtra("food_name").toString()
        println("FFFFFFFFFFFFF$foodName")
        val db = FirebaseFirestore.getInstance()
        var energy : Int = 1
        var serving_size = 1
        var unit_id = 1
        setSupportActionBar(this.findViewById(R.id.mtoolbar))
        toolbar = supportActionBar!!
        toolbar.title = "แก้ไขรายการอาหาร"
        toolbar.setDisplayHomeAsUpEnabled(true)

        val getcalorie = db.collection("FOOD_TABLE").whereEqualTo("food_nameth",foodName)
        getcalorie.get().addOnSuccessListener {doc ->
            for (document in doc) {
                val datahash = document.data
                energy = datahash["energy"].toString().toInt()
                serving_size = datahash["serving_size"].toString().toInt()
                unit_id = datahash["unit_id"].toString().toInt()
            }
        }
            .addOnFailureListener {
                Log.d(TAG,"Get Value Fail")
            }

        val getUnittype = db.collection("UNIT_TABLE").whereEqualTo("unit_id", unit_id)
        getUnittype.get().addOnSuccessListener {doc ->
            for (document in doc) {
                val datahash = document.data
                unit_type.text = datahash["unit_name"].toString()
            }
        }
            .addOnFailureListener {
                Log.d(TAG,"Get data type Fail")
            }

        val getdataById = db.collection("FOODCONSUME_TABLE").document(foodConsumeId)
        getdataById.get().addOnSuccessListener {

            if (it != null) {
                val hashFoodDetail = it.data as HashMap<String, Any>
                eDCFood_namethText.text = foodName
                eDCservingSize.setText(hashFoodDetail["serving_size"].toString(), TextView.BufferType.EDITABLE)
            }
        }


        eDCSaveCalorie.setOnClickListener {
            val serving_SizeED = eDCservingSize.text.toString().toInt()
            val caltotal = serving_SizeED * energy/serving_size



            val builder = AlertDialog.Builder(this)
            builder.setView(R.layout.dialog_addcal)
            builder.setTitle("ยืนยันการแก้ไขแคลลอรี่")

            builder.setNegativeButton("ยกเลิก") {dialog, which ->
                Toast.makeText(this,"No", Toast.LENGTH_SHORT).show()
            }
            builder.setPositiveButton("บันทึก"){dialog, which ->
                addFoodconsume(caltotal,serving_SizeED,foodConsumeId)

                Toast.makeText(this,"กำลังบันทึกข้อมูล", Toast.LENGTH_SHORT).show()
                //ถ้า cash เอาออกแล้วใส่ Toast แทน

            }
            val dialog : AlertDialog = builder.create()
            dialog.show()

            val dialog_foodname = dialog.findViewById<TextView>(R.id.dialog_foodname)
            val dialog_servingsize = dialog.findViewById<TextView>(R.id.dialog_eattotal)
            val dialog_typeunit = dialog.findViewById<TextView>(R.id.dialog_type)
            val dialog_totalcal = dialog.findViewById<TextView>(R.id.dialog_caltotal)
            dialog_foodname!!.text = foodName
            dialog_servingsize!!.text = serving_SizeED.toString()
            dialog_typeunit!!.text = unit_type.text.toString()
            dialog_totalcal!!.text = caltotal.toString()

            val dialog_title = dialog.findViewById<TextView>(R.id.textview00)
            dialog_title!!.text = "รายการที่ต้องการแก้ไข"
        }

    }

    private fun addFoodconsume(caltotal: Int, serving_size: Int, foodConsumeId: String) {

        val db = FirebaseFirestore.getInstance()
        val updateData = db.collection("FOODCONSUME_TABLE").document(foodConsumeId)

        val foodConHash = HashMap<String,Any>()
        foodConHash["calorie_total"] = caltotal
        foodConHash["serving_size"] = serving_size

        updateData.update(foodConHash).addOnSuccessListener {
            Toast.makeText(this,"บันทึกข้อมูลสำเร็จ",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,MainActivity::class.java))
            this.finish()
        }.addOnFailureListener {
            Toast.makeText(this,"ล้มเหลว",Toast.LENGTH_SHORT).show()
        }

    }
}
