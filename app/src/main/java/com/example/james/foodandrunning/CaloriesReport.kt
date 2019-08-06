package com.example.james.foodandrunning

import android.content.Context
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.example.james.foodandrunning.setupdata.AppPreferences
import com.example.james.foodandrunning.setupdata.FoodTotalCal
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_caloriesreport.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


private val TAG = "CaloriesReport "

class CaloriesReport : AppCompatActivity() {

    lateinit var toolbar : ActionBar
    var runningcal = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caloriesreport)

        getRuningCalories()

    }

    private fun getRuningCalories() {

        val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val sharedPreferences = this.getSharedPreferences("date", Context.MODE_PRIVATE)
        val countDay = sharedPreferences.getInt("countDay",0)
        println("countDay:$countDay")
        val calendar =  Calendar.getInstance()
        calendar.add(Calendar.DATE,countDay)
        val getDate = calendar.time

        val calendarlimit = Calendar.getInstance()
        calendarlimit.add(Calendar.DATE,countDay+1)
        val getDatlimit = calendarlimit.time

        val timeBase = formatter.parse(formatter.format(getDate))
        val timeLimit = formatter.parse(formatter.format(getDatlimit))
        FirebaseFirestore.getInstance().collection("EXERCISE_TABLE")
            .whereGreaterThanOrEqualTo("running_date", timeBase)
            .whereLessThanOrEqualTo("running_date",timeLimit).get().addOnSuccessListener {
                for (data in it) {
                    val datahash = data.data
                    runningcal += datahash["running_calorie"].toString().toDouble()
                }
                getChart()
            }
            .addOnFailureListener {
                getChart()
                Log.d(TAG , "getRunning fail")
            }
    }

    private fun getChart() {
        val pieChart = findViewById<PieChart>(R.id.calchart)
        val CalFoodArrayList = ArrayList<FoodTotalCal>()

        val bf = intent.getStringExtra("bf").toString().toFloat()
        val lu = intent.getStringExtra("lu").toString().toFloat()
        val dn = intent.getStringExtra("din").toString().toFloat()
        val sn = intent.getStringExtra("sn").toString().toFloat()

        //set titel
        setSupportActionBar(findViewById(R.id.mtoolbar))
        toolbar = supportActionBar!!
        toolbar.title = "สรุปแคลลอรี่ประจำวัน"
        toolbar.setDisplayHomeAsUpEnabled(true)


        val entries = ArrayList<PieEntry>()
        val caltotaltext = findViewById<TextView>(R.id.calcharttotal)
        val totaleat = AppPreferences(this).getPreferenceTotalEat()
        val fullcalories = AppPreferences(this).getPreferenceCal()
        caltotaltext.text = totaleat.toString()

        if (bf > 0)CalFoodArrayList.add(FoodTotalCal("มื้อเช้า",bf))
        if (lu > 0)CalFoodArrayList.add(FoodTotalCal("มื้อเที่ยง",lu))
        if (dn > 0)CalFoodArrayList.add(FoodTotalCal("มื้อเย็น",dn))
        if (sn > 0)CalFoodArrayList.add(FoodTotalCal("ขนมขบเคี้ยว",sn))
        if (runningcal > 0) {
            CalFoodArrayList.add(FoodTotalCal("แคลลอรี่ที่วิ่ง", runningcal.toFloat()))
            runningText.text = String.format("%.2f", runningcal)
        }
        if (fullcalories - (runningcal + totaleat) > 0) {
            val eatsub = fullcalories - runningcal - totaleat
            eatsubcal.text = String.format("%.2f", eatsub)
        }


        for (i in CalFoodArrayList) {
            entries.add(PieEntry(i.value,i.name))
        }

        val dataset = PieDataSet(entries, "แคลลอรี่อาหารที่รับประทาน")

        dataset.selectionShift = 10.toFloat()
        dataset.valueTextSize = 14.toFloat()
        dataset.setColors(ColorTemplate.MATERIAL_COLORS.toMutableList())

        val data = PieData(dataset)
        pieChart.data = data
        pieChart.holeRadius = 30.toFloat()
        pieChart.transparentCircleRadius = 40.toFloat()
        pieChart.animateY(2000)
    }
}


