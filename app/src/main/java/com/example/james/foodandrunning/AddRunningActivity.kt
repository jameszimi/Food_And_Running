package com.example.james.foodandrunning

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlinx.android.synthetic.main.activity_addrunning.*
import android.widget.Toast
import com.example.james.foodandrunning.firebase.auth.FirestoreRunnigAuth


class AddRunningActivity : AppCompatActivity() {

    lateinit var toolbar: ActionBar
    var hrValue = 0
    var minValue = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addrunning)

        setSupportActionBar(findViewById(R.id.mtoolbar))
        toolbar = supportActionBar!!
        toolbar.title = "เพิ่มรายการวิ่ง"
        toolbar.setDisplayHomeAsUpEnabled(true)



        val mListofHr = findViewById<Spinner>(R.id.spinnerHr)
        val mListofMin = findViewById<Spinner>(R.id.spinnerMin)
        val listofHr = listofHr()
        val listofMin = listofMin()

        mListofHr.adapter = ArrayAdapter<Int>(this, android.R.layout.simple_gallery_item,listofHr)
        mListofMin.adapter = ArrayAdapter<Int>(this, android.R.layout.simple_gallery_item, listofMin)

        mListofHr.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                hrValue = listofHr[position]
            }

        }

        mListofMin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                minValue = listofMin[position]
            }

        }




        //editText.addTextChangedListener(object : TextWatcher {
        //            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        //
        //            }
        //
        //            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        //
        //            }
        //
        //            override fun afterTextChanged(editable: Editable) {
        //                if (editText == null) return
        //                val inputString = editable.toString()
        //                editText.removeTextChangedListener(this)
        //                val cleanString = inputString.replace("[.]".toRegex(), "")
        //                val bigDecimal = BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR)
        //                    .divide(BigDecimal(100), BigDecimal.ROUND_FLOOR)
        //                val converted = NumberFormat.getNumberInstance().format(bigDecimal).replaceAll("[,]", "")
        //                editText.setText(converted)
        //                editText.setSelection(converted.length)
        //                editText.addTextChangedListener(this)
        //            }
        //        })


        addrunningBtn.setOnClickListener {
            getdataAtActivity()
        }


    }

    fun listofHr() : ArrayList<Int> {

        val hrvalue = ArrayList<Int>()
        for (i in 0..12) {
            hrvalue.add(i)
        }
        return hrvalue

    }

    fun listofMin() : ArrayList<Int> {

        val minvalue = ArrayList<Int>()
        for (i in 0..60) {
            minvalue.add(i)
        }
        return minvalue

    }

    private fun getdataAtActivity() {

        var distance  = 00.00
        var time  = 00.00
        var calories  = 00.00

        if (distanceEDText.text.isNotEmpty()){
            distance = distanceEDText.text.toString().toDouble()
        }

        if (calEDText.text.isNotEmpty()) {
            calories = calEDText.text.toString().toDouble()
        }
        if (distanceEDText.text.isEmpty() && (minValue == 0) && calEDText.text.isEmpty()) {
            return Toast.makeText(this,"กรุณากรอกข้อมูลก่อนทำการกดบันทึก",Toast.LENGTH_SHORT).show()
        }
        if ((
                    (distanceEDText.text.isEmpty() && (hrValue != 0 && minValue == 0)) ||
                    (distanceEDText.text.isEmpty() && (minValue == 0 && hrValue != 0)) ||
                    (distanceEDText.text.isNotEmpty() && (hrValue == 0 && minValue == 0)))
            && calEDText.text.isEmpty()) {
            return Toast.makeText(this,"กรุณากรอกระยะทางและเวลา",Toast.LENGTH_SHORT).show()
        }

        time = ((hrValue*60)+minValue).toDouble()
        FirestoreRunnigAuth(this).addRunningManual(distance, time, calories)

    }
}
