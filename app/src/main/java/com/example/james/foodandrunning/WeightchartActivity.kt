package com.example.james.foodandrunning

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
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
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.util.*
import kotlin.collections.ArrayList
import kotlin.text.Typography.times

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
                    val datatime = id.data as Map<String,Timestamp>
                    weightIdList.add(WeightId(datatime["weight_update"]!!.seconds,dataHash["weight_value"].toString().toInt()))
                    val nano = (datatime["weight_update"]!!.seconds)
                            println(TAG+"ddddd:" + nano)
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

        /*val leftAxis = chart.axisLeft
        leftAxis.textColor = ColorTemplate.getHoloBlue()
        leftAxis.axisMaximum = 150f
        leftAxis.axisMinimum = 0f
        leftAxis.setDrawGridLines(false)
        leftAxis.isGranularityEnabled = true*/


        val calendar = Calendar.getInstance()

        val daynow = calendar.timeInMillis.toFloat()
        val lastmil : Float

        val count = weightIdList.size
        val range = 50f
        val values1 = ArrayList<Entry>()
        val floatArray = FloatArray(7)


        var i = 0
        var countindex = count-1
        while (i < count)
        {
            //val val1 = (Math.random() * (range)) + 50
            values1.add(Entry(i.toFloat(), weightIdList[countindex].weightValue.toFloat()))
            println(TAG+"index:$i weightIdList[i]:"+weightIdList[i].weightValue.toFloat())
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
            //set1.setFillFormatter(new MyFillFormatter(0f));
            //set1.setDrawHorizontalHighlightIndicator(false);
            //set1.setVisible(false);
            //set1.setCircleHoleColor(Color.WHITE);

            val data = LineData(set1)
            data.setValueTextColor(Color.RED)
            data.setValueTextSize(9f)

            // set data
            chart.data = data
    }

}

    /*private fun setGraph(weightIdList: ArrayList<WeightId>) {

        val calendar = Calendar.getInstance()
        val b1 = calendar.time
        val calendarmin = Calendar.getInstance()
        calendarmin.add(Calendar.MONTH,-1)
        val b2 = calendarmin.time

        calendarmin.clear()
        val count = weightIdList.size
        calendarmin.add(Calendar.MILLISECOND,weightIdList[count-1].dateWeight.toInt())
        val d7 = calendarmin.time
        calendarmin.clear()
        calendarmin.add(Calendar.MILLISECOND,weightIdList[count-2].dateWeight.toInt())
        val d6 = calendarmin.time
        calendarmin.clear()
        calendarmin.add(Calendar.MILLISECOND,weightIdList[count-3].dateWeight.toInt())
        val d5 = calendar.time




        println(TAG+"d7:$d7 d6:$d6 d5:$d5")
        println(TAG+"d1:$b1")
        println(TAG+"d2:$b2")
        //       val d3 = calendar.time
        println(TAG+count)

        val w1 = weightIdList[count-1].weightValue.toDouble()
        val w2 = weightIdList[count-2].weightValue.toDouble()
        val w3 = weightIdList[count-3].weightValue.toDouble()

        val predata = arrayOf(
            DataPoint(d7.time.toDouble(),w1),
            DataPoint(d6.time.toDouble(),w2),
            DataPoint(d5.time.toDouble(),w3)
        )

        val graphView = findViewById<GraphView>(R.id.graphView)

        println(TAG+"weightIdList:$weightIdList")


        //for (index in 1..weightIdList.size){
          //  val v = DataPoint(timestamp)
        //}
        val series = LineGraphSeries<DataPoint>(predata)

        println(TAG+"predata:$predata")
        //arrayOf(
        //                DataPoint(0.toDouble(), 1.toDouble()),
        //                DataPoint(1.toDouble(), 5.toDouble()),
        //                DataPoint(2.toDouble(), 1.toDouble()))

        println(TAG+"series:$series")
        graphView.addSeries(series)

        // set date label formatter
        graphView.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(this)
        graphView.gridLabelRenderer.numHorizontalLabels = 5

        // set manual x bounds to have nice steps
        graphView.viewport.setMinX(b2.time.toDouble())
        graphView.viewport.setMaxX(b1.time.toDouble())
        graphView.viewport.isXAxisBoundsManual = true

        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        graphView.gridLabelRenderer.setHumanRounding(false)
    }

    private fun DP(i: Int, i1: Int): DataPoint {
        return DataPoint(i.toDouble(),i1.toDouble())

    }*/
}
