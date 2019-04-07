package com.example.james.foodandrunning.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.james.foodandrunning.R
import com.example.james.foodandrunning.UserfoodcreatActivity
import com.example.james.foodandrunning.firebase.auth.FirestoreFoodConsumeAuth
import com.example.james.foodandrunning.setupdata.Userfood
import com.google.gson.Gson
import kotlinx.android.synthetic.main.userfood_row.view.*

class UserFoodAdapterList(val mExampleList: ArrayList<Userfood>) : RecyclerView.Adapter<CustomViewHolderList> (){

    override fun getItemCount(): Int {
        return mExampleList.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolderList {
        val layoutInflater = LayoutInflater.from(p0.context)
        val cellForRow = layoutInflater.inflate(R.layout.userfood_row, p0, false)
        return CustomViewHolderList(cellForRow)
    }

    override fun onBindViewHolder(p0: CustomViewHolderList, p1: Int) {
        val userFoodName = mExampleList[p1].foodName
        val userFoodCal = mExampleList[p1].totalCal
        println("DDDDDDDDDDDDDDDDDD:"+mExampleList[p1])
        p0.view.userFoodText.text = userFoodName
        p0.view.userCalText.text = userFoodCal.toString()

        p0.view.setOnClickListener {
            FirestoreFoodConsumeAuth(p0.view.context).userCreateFood(userFoodName,userFoodCal)
        }

        p0.itemView.setOnLongClickListener {

            val builder = AlertDialog.Builder(p0.view.context)
            builder.setTitle("ลบ")
            builder.setNegativeButton("ยกเลิก"){ dialog, which ->
            }
            builder.setPositiveButton("ตกลง") {dialog, which ->
                val sharedPreferences = p0.view.context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                val gson = Gson()
                mExampleList.removeAt(p1)
                val json = gson.toJson(mExampleList)
                editor.putString("userfoodcreate", json)
                editor.apply()
                val clickIntent = Intent(p0.view.context, UserfoodcreatActivity::class.java)
                p0.view.context.startActivity(clickIntent)
            }
            builder.show()

            return@setOnLongClickListener true
        }


        //ถ้าต้องการลบ
        /*
        p0.itemView.setOnLongClickListener {

            val builder = AlertDialog.Builder(p0.view.context)
            builder.setView(R.layout.dialog_setfoodlist)
            builder.setTitle("เลือกทำรายการ")
            val dialog : AlertDialog = builder.create()
            dialog.show()

            val dialogEditFood = dialog.findViewById<TextView>(R.id.setfoodlist_edit)
            val dialogDelFood = dialog.findViewById<TextView>(R.id.setfoodlist_del)


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
            }*/

    }
}
