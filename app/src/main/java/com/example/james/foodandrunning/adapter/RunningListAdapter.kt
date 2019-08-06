package com.example.james.foodandrunning.adapter

import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.james.foodandrunning.R
import com.example.james.foodandrunning.firebase.auth.FirestoreRunnigAuth
import com.example.james.foodandrunning.setupdata.RunningDataList
import kotlinx.android.synthetic.main.dialog_runninglist.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class RunningList(val rnList: ArrayList<RunningDataList>) : RecyclerView.Adapter<CustomViewHolderFoodList>(){

    override fun getItemCount(): Int {
        return rnList.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolderFoodList {

        val layoutInflater = LayoutInflater.from(p0.context)
        val cellForRow = layoutInflater.inflate(R.layout.dialog_runninglist,p0,false)
        return CustomViewHolderFoodList(cellForRow)

    }

    override fun onBindViewHolder(p0: CustomViewHolderFoodList, p1: Int) {


        val day = rnList[p1].day //before NameList.get[p1]
        val distance = rnList[p1].distance
        val calories = rnList[p1].calories
        val runningId = rnList[p1].runningId

        //val  dayon = day.toString()
        //val formatter = SimpleDateFormat("dd/MM/yyyy")
        //val daytime = formatter.parse(formatter.format(dayon))


        //p0.view.runningday.text = daytime.toString()
        val formatter: DateFormat = SimpleDateFormat("วันที่ dd/MM/yyyy เวลา HH:mm")
        p0.view.runningday.text = formatter.format(day)
        p0.view.distancedialog.text = String.format("ระยะทาง %.2f เมตร",distance)
        p0.view.caloriesdialog.text = String.format("%.2f แคลลอรี่",calories)

        p0.itemView.setOnLongClickListener {
            val builder = AlertDialog.Builder(p0.view.context)
            builder.setTitle("ลบรายการนี้")
            builder.setNegativeButton("ยกเลิก"){ dialog, which ->
            }
            builder.setPositiveButton("ตกลง") {dialog, which ->
                Toast.makeText(p0.view.context,"ลบสำเร็จ",Toast.LENGTH_SHORT).show()
                FirestoreRunnigAuth(p0.view.context).deleateRunningPath(runningId)
            }
            builder.show()
            return@setOnLongClickListener true
        }
        //p0.itemView.setOnLongClickListener() {
        //}
    }

}

class CustomViewHolderRunningList(val view: View): RecyclerView.ViewHolder(view){

}