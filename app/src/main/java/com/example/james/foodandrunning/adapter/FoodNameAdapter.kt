package com.example.james.foodandrunning.adapter

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.james.foodandrunning.AdmineditfoodActivity
import com.example.james.foodandrunning.R
import com.example.james.foodandrunning.firebase.auth.FirestoreRunnigAuth
import com.example.james.foodandrunning.setupdata.FoodName
import com.example.james.foodandrunning.setupdata.RunningDataList
import kotlinx.android.synthetic.main.dialog_foodeditlist.view.*
import kotlinx.android.synthetic.main.dialog_runninglist.view.*
import java.util.ArrayList

class FoodNameAdapter(val rnList: ArrayList<FoodName>) : RecyclerView.Adapter<CustomViewHolderFoodList>(){

    override fun getItemCount(): Int {
        return rnList.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolderFoodList {

        val layoutInflater = LayoutInflater.from(p0.context)
        val cellForRow = layoutInflater.inflate(R.layout.dialog_foodeditlist,p0,false)
        return CustomViewHolderFoodList(cellForRow)

    }

    override fun onBindViewHolder(p0: CustomViewHolderFoodList, p1: Int) {


        val foodname = rnList[p1].foodName
        val foodid = rnList[p1].food_id

        p0.view.foodname_dialogedit.text = foodname

        p0.itemView.setOnClickListener {
            val intent = Intent(p0.view.context,AdmineditfoodActivity::class.java)
            intent.putExtra("food_id",foodid)
            p0.view.context.startActivity(intent)
            (p0.view.context as Activity).finish()
        }
        //p0.itemView.setOnLongClickListener() {
        //}
    }

}

class CustomViewHolderFoodList(val view: View): RecyclerView.ViewHolder(view){

}