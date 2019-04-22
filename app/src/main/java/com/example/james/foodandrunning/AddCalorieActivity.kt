package com.example.james.foodandrunning

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_calorie.*
import kotlinx.android.synthetic.main.dialog_addcal.*
import android.view.View
import com.example.james.foodandrunning.setupdata.AppPreferences
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class AddCalorieActivity : AppCompatActivity() {

    lateinit var toolbar : ActionBar

    val TAG = "AddCalorieActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_calorie)

        val meal = intent.getStringExtra("meal").toString().toInt()
        println("AddCalorieActivity : meal:$meal")
        val food_id = intent.getStringExtra("food_id").toString()
        println("AddCalorieActivity : String:$food_id")
        var energy  = 1.0
        var serving_size = 1.0
        var unit_id = 1
        var food_nameth = ""

        println("meal : $meal, food_id : $food_nameth")

        val db = FirebaseFirestore.getInstance()
        val getdata = db.collection("FOOD_TABLE").document(food_id)
            getdata.get().addOnSuccessListener {doc ->
                if (doc != null){
                    val datahash = doc.data
                    energy = datahash!!["energy"].toString().toDouble()
                    serving_size = datahash!!["serving_size"].toString().toDouble()
                    unit_id = datahash!!["unit_id"].toString().toInt()
                    food_nameth = datahash!!["food_nameth"].toString()
                }

                eDCFood_namethText.text = food_nameth
                getUnitName(unit_id)
        }
            .addOnFailureListener {
                Log.d(TAG,"Get Value Fail")
            }



        println(TAG+"bbbbbbbbbb"+meal+food_id)

        setSupportActionBar(findViewById(R.id.mtoolbar))
        toolbar = supportActionBar!!
        toolbar.title = "บันทึกแคลลอรี่"
        toolbar.setDisplayHomeAsUpEnabled(true)


        println(TAG+"aaaaaaaaaaaaaaaa"+dialog_foodname)




        eDCSaveCalorie.setOnClickListener {


            val serving_SizeED = eDCservingSize.text.toString().toInt().toDouble()
            val caltotal= serving_SizeED * energy/serving_size
            println("eDCSaveCalorie serving_sizeed:$serving_SizeED, energy:$energy, serving_size:$serving_size, Caltotal:$caltotal" )



            val builder = AlertDialog.Builder(this)
            builder.setView(R.layout.dialog_addcal)
            builder.setTitle("ยืนยันการเพิ่มแคลลอรี่")

            builder.setNegativeButton("ยกเลิก") {dialog, which ->
                Toast.makeText(this,"No",Toast.LENGTH_SHORT).show()
            }
            builder.setPositiveButton("บันทึก"){dialog, which ->
                addFoodconsume(meal,caltotal.toInt(),food_id,serving_size.toInt())

                Toast.makeText(this,"กำลังบันทึกข้อมูล",Toast.LENGTH_SHORT).show()
                //ถ้า cash เอาออกแล้วใส่ Toast แทน

            }
            val dialog : AlertDialog = builder.create()
            dialog.show()

            val dialog_foodname = dialog.findViewById<TextView>(R.id.dialog_foodname)
            val dialog_servingsize = dialog.findViewById<TextView>(R.id.dialog_eattotal)
            val dialog_typeunit = dialog.findViewById<TextView>(R.id.dialog_type)
            val dialog_totalcal = dialog.findViewById<TextView>(R.id.dialog_caltotal)
            dialog_foodname!!.text = food_nameth
            dialog_servingsize!!.text = serving_SizeED.toString()
            dialog_typeunit!!.text = unit_type.text.toString()
            dialog_totalcal!!.text = String.format("%.2f",caltotal)
        }


    }

    private fun getUnitName(unit_id: Int) {

        val db = FirebaseFirestore.getInstance()
        val getUnittype = db.collection("UNIT_TABLE").whereEqualTo("unit_id", unit_id)
        getUnittype.get().addOnSuccessListener {doc ->
            for (document in doc) {
                val datahash = document.data
                unit_type.text = datahash["unit_name"].toString()
                println("unit_type="+datahash["unit_name"].toString())
            }
        }
            .addOnFailureListener {
                Log.d(TAG,"Get data type Fail")
            }

    }

    private fun addFoodconsume(
        meal: Int,
        caltotal: Int,
        food_id: String,
        serving_size: Int
    ) {

        val progessbar = findViewById<ProgressBar>(R.id.progessBar_addcal)
        if (progessbar != null) {
            val visibility = if (progessbar.visibility == View.GONE) {
                View.VISIBLE
            } else View.GONE
            progessbar.visibility = visibility
        }


        val foodcondata = HashMap<String, Any>()
        val appPreferences = AppPreferences(this)
        val uid = appPreferences.getPreferenceUID()
        val db = FirebaseFirestore.getInstance()
        val addFoodConsumeToDb = db.collection("FOODCONSUME_TABLE").document()
        val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val today = Date()
        val todayWithZeroTime = formatter.parse(formatter.format(today))

        println(TAG + " todayWithZeroTime "+todayWithZeroTime)

        foodcondata["food_id"] = food_id
        foodcondata["member_id"] = uid
        foodcondata["repast_id"] = meal
        foodcondata["calorie_total"] = caltotal
        foodcondata["foodconsume_date"] = todayWithZeroTime
        foodcondata["serving_size"] = serving_size

        println(TAG+" foodcondata "+foodcondata)

        addFoodConsumeToDb.set(foodcondata).addOnSuccessListener {
            Toast.makeText(this,"บันทึกข้อมูลสำเร็จ",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            this.finish()
        }.addOnFailureListener {
            Toast.makeText(this,"ล้มเหลว",Toast.LENGTH_SHORT).show()
        }




    }
}
