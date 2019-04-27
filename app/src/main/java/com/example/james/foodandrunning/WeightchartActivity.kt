package com.example.james.foodandrunning

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import com.example.james.foodandrunning.setupdata.AppPreferences
import com.example.james.foodandrunning.setupdata.WeightId
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.LineData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*
import kotlin.collections.ArrayList

lateinit var toolbar : ActionBar

class WeightchartActivity : AppCompatActivity() {

    private val TAG = "WeightchartActivity: "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weightchart)

        setSupportActionBar(findViewById(R.id.mtoolbar))
        toolbar = supportActionBar!!
        toolbar.title = "แสดงกราฟน้ำหนักย้อนหลัง"
        toolbar.setDisplayHomeAsUpEnabled(true)

            val weightIdList = ArrayList<WeightId>()
            val uid = AppPreferences(this).getPreferenceUID()
            val db = FirebaseFirestore.getInstance()
            db.collection("WEIGHT_TABLE").whereEqualTo("member_uid",uid).orderBy("weight_update", Query.Direction.DESCENDING).limit(7).get().addOnSuccessListener { it ->

                println(TAG+"SUCCESS")
                for (id in it) {
                    val dataHash = id.data
                    val date = dataHash["weight_update"] as Timestamp
                    weightIdList.add(WeightId(date.toDate(),dataHash["weight_value"].toString().toFloat()))

                    println(TAG+"weightIdList with dataHash:$weightIdList")
                }
                println(TAG+"weightIdList:$weightIdList")
                println(TAG+"weightIdList.size:${weightIdList.size}")
                setGraph(weightIdList)
            }.addOnFailureListener {
                println(TAG+"Fail")
            }









        }

    private fun setGraph(weightIdList: ArrayList<WeightId>) {

        val chart = findViewById<LineChart>(R.id.weightchart)
        chart.setTouchEnabled(true)
        chart.setBackgroundColor(Color.WHITE)

        val l = chart.legend
        l.form = Legend.LegendForm.LINE
        l.textSize = 11f
        l.textColor = Color.RED
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)

        val xAxis = chart.xAxis
        xAxis.textSize = 11f
        xAxis.textColor = ColorTemplate.getHoloBlue()
        xAxis.axisMaximum = weightIdList.size.toFloat()
        xAxis.axisMinimum = 0f
        xAxis.setDrawGridLines(false)
        xAxis.isGranularityEnabled = false



        val calendar = Calendar.getInstance()

        val daynow = calendar.timeInMillis.toFloat()
        //al lastmil : Float

        val count = weightIdList.size
        //val range = 50f
        val values1 = ArrayList<Entry>()
        //val floatArray = FloatArray(7)


        var i = 0
        var countindex = count-1
        while (i < count)
        {
            //val val1 = (Math.random() * (range)) + 50
            values1.add(Entry(i.toFloat(), weightIdList[countindex].weightValue))
            println(TAG+"index:$i weightIdList[i]:"+weightIdList[i].weightValue)
            i += 1
            countindex -= 1
        }
        println(TAG+"values1:$values1")

        if ((chart.data != null && chart.data.dataSetCount > 0)) {
            val set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values1
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            val set1 = LineDataSet(values1, "น้ำหนักย้อนหลัง")

            set1.axisDependency = AxisDependency.LEFT
            set1.color = ColorTemplate.getHoloBlue()
            set1.setCircleColor(Color.DKGRAY)
            set1.lineWidth = 2f
            set1.circleRadius = 3f
            set1.fillAlpha = 65
            set1.fillColor = ColorTemplate.getHoloBlue()
            set1.highLightColor = Color.rgb(244, 117, 117)
            set1.setDrawCircleHole(false)

            val data = LineData(set1)
            data.setValueTextColor(Color.RED)
            data.setValueTextSize(9f)

            // set data
            chart.data = data
    }

}
}
