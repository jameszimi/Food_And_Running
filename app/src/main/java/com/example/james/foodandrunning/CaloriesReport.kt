package com.example.james.foodandrunning

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieEntry
import android.widget.TextView
import com.example.james.foodandrunning.setupdata.FoodTotalCal
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import java. util. ArrayList


private val TAG = "CaloriesReport "

class CaloriesReport : AppCompatActivity() {

    lateinit var toolbar : ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caloriesreport)

        val pieChart = findViewById<PieChart>(R.id.calchart)
        val CalFoodArrayList = ArrayList<FoodTotalCal>()

        //val student : ArrayList<String>
        val bf = intent.getStringExtra("bf").toString().toFloat()
        val lu = intent.getStringExtra("lu").toString().toFloat()
        val dn = intent.getStringExtra("din").toString().toFloat()
        val sn = intent.getStringExtra("sn").toString().toFloat()

        println(TAG + "$bf $lu $dn $sn")
        //set titel
        setSupportActionBar(findViewById(R.id.mtoolbar))
        toolbar = supportActionBar!!
        toolbar.title = "สรุปแคลลอรี่ประจำวัน"
        toolbar.setDisplayHomeAsUpEnabled(true)



        val entries = ArrayList<PieEntry>()
        val caltotaltext = findViewById<TextView>(R.id.calcharttotal)
        caltotaltext.text = intent.getStringExtra("totalCal")

        if (bf > 0)CalFoodArrayList.add(FoodTotalCal("มื้อเช้า",bf))
        if (lu > 0)CalFoodArrayList.add(FoodTotalCal("มื้อเที่ยง",lu))
        if (dn > 0)CalFoodArrayList.add(FoodTotalCal("มื้อเย็น",dn))
        if (sn > 0)CalFoodArrayList.add(FoodTotalCal("ขนมขบเคี้ยว",sn))


        println(TAG+CalFoodArrayList)
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





        //dataset.color = ColorTemplate.MATERIAL_COLORS



    }
}


