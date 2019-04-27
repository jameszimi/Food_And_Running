package com.example.james.foodandrunning.adapter

import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.example.james.foodandrunning.EditDetailFoodCon
import com.example.james.foodandrunning.MainActivity
import com.example.james.foodandrunning.R
import com.example.james.foodandrunning.setupdata.FoodNCal
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.listfood_cal.view.*

class BreakfastList(val bfList: ArrayList<FoodNCal>) :RecyclerView.Adapter<CustomViewHolderFoodList>(){

    override fun getItemCount(): Int {
        return bfList.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolderFoodList {

        val layoutInflater = LayoutInflater.from(p0.context)
        val cellForRow = layoutInflater.inflate(R.layout.listfood_cal,p0,false)
        return CustomViewHolderFoodList(cellForRow)

    }

    override fun onBindViewHolder(p0: CustomViewHolderFoodList, p1: Int) {

        val foodName = bfList[p1].foodName //before NameList.get[p1]
        val foodcal = bfList[p1].totalCal
        val foodConsumeId = bfList[p1].foodConsume
        p0.view.food_name.text = foodName
        p0.view.food_cal.text = foodcal.toString()

        p0.itemView.setOnLongClickListener {

            val builder = AlertDialog.Builder(p0.view.context)
            builder.setView(R.layout.dialog_setfoodlist)
            builder.setTitle("เลือกทำรายการ")
            val dialog : AlertDialog = builder.create()
            dialog.show()

            val dialogEditFood = dialog.findViewById<TextView>(R.id.setfoodlist_edit)
            val dialogDelFood = dialog.findViewById<TextView>(R.id.setfoodlist_del)

            if (bfList[p1].foodName == "รายการอาหารที่ไม่มีอยู่ในระบบ"){
                dialogEditFood!!.visibility = View.GONE
            }

            dialogEditFood!!.setOnClickListener {

                val clickIntent = Intent(p0.view.context, EditDetailFoodCon::class.java)
                clickIntent.putExtra("foodConsumeId",foodConsumeId)
                clickIntent.putExtra("food_name",foodName)
                p0.view.context.startActivity(clickIntent)

                Toast.makeText(p0.view.context,"แก้ไข",Toast.LENGTH_SHORT).show()
            }

            dialogDelFood!!.setOnClickListener {
                dialog.dismiss()
                builder.setView(null)
                builder.setTitle("ยืนยันการลบ")
                builder.setNegativeButton("ยกเลิก") { dialog, which ->
                    Toast.makeText(p0.view.context,"ยกเลิกการลบ",Toast.LENGTH_SHORT).show()
                }
                builder.setPositiveButton("ยืนยัน") { dialog, which ->
                    val db = FirebaseFirestore.getInstance()
                    db.collection("FOODCONSUME_TABLE").document(foodConsumeId).delete().addOnSuccessListener {
                        val clickIntent = Intent(p0.view.context, MainActivity::class.java)
                        p0.view.context.startActivity(clickIntent)
                        Toast.makeText(p0.view.context,"ลบสำเร็จ",Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(p0.view.context,"ลบไม่สำเร็จ",Toast.LENGTH_SHORT).show()
                    }

                }
                builder.show()
                Toast.makeText(p0.view.context,"ลบ",Toast.LENGTH_SHORT).show()
            }

            Toast.makeText(p0.view.context,foodConsumeId,Toast.LENGTH_SHORT).show()

            return@setOnLongClickListener true
        }
        //p0.itemView.setOnLongClickListener() {
        //}
    }

}

class CustomViewHolderList(val view:View):RecyclerView.ViewHolder(view){

}


