package com.example.james.foodandrunning.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.james.foodandrunning.AddCalorieActivity
import com.example.james.foodandrunning.R
import kotlinx.android.synthetic.main.search_row.view.*

class SearchFoodAdapter(arrayofData: MutableSet<String>, meal: String) :RecyclerView.Adapter<CustomViewHolder>() {

    val listOfFood = arrayofData.toTypedArray()
    val meals = meal

    //numberOfItem
    override fun getItemCount(): Int {
        return listOfFood.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(p0.context)
        val cellForRow = layoutInflater.inflate(R.layout.search_row, p0, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(p0: CustomViewHolder, p1: Int) {

        val foodName = listOfFood[p1]
        p0.view.foodname_text.text = foodName
        p0.itemView.setOnClickListener {
            println("bbbbbbbbbbbbbbbbbbb "+foodName)
            Toast.makeText(p0.view.context,foodName,Toast.LENGTH_SHORT).show()
            val clickIntent = Intent(p0.view.context, AddCalorieActivity::class.java)
            clickIntent.putExtra("food_nameth",foodName)
            clickIntent.putExtra("meal",meals)
            p0.view.context.startActivity(clickIntent)
        }
    }
}

class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view) {

}