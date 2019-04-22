package com.example.james.foodandrunning

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import android.widget.Toolbar
import com.example.james.foodandrunning.adapter.RunningList
import com.example.james.foodandrunning.firebase.auth.FirestoreRunnigAuth
import com.example.james.foodandrunning.setupdata.AppPreferences
import com.example.james.foodandrunning.setupdata.RunningDataList
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.type.Date
import java.text.SimpleDateFormat
import java.util.*

class RunningListActivity : AppCompatActivity() {

    private var queryGet = FirebaseFirestore.getInstance().collection("EXERCISE_TABLE")
    val runningDataList = ArrayList<RunningDataList>()
    lateinit var toolbar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_runninglist)

        getRunningData()
        setSupportActionBar(findViewById(R.id.mtoolbar))
        toolbar = supportActionBar!!
        toolbar.title = "รายการวิ่ง"
        toolbar.setDisplayHomeAsUpEnabled(true)


    }

    private fun getRunningData(){
        val uid = AppPreferences(this).getPreferenceUID()
        println("getRunningData uid:$uid")
        queryGet.whereEqualTo("member_id",uid).orderBy("running_date",Query.Direction.DESCENDING).get().addOnSuccessListener {
                println("getRunningData innnn")
                for (data in it){
                    val dataHash = data.data
                    runningDataList.add(RunningDataList(dataHash["running_date"],
                        dataHash["running_distance"],dataHash["running_calorie"],
                        data.id))
                    println("getRunningData datahash = $dataHash")
                }
                val runningRecyclerView = findViewById<RecyclerView>(R.id.runningrecycler)
                runningRecyclerView.layoutManager = LinearLayoutManager(this)
                runningRecyclerView.adapter = RunningList(runningDataList)
            }
            .addOnFailureListener {
                Toast.makeText(this,"FAILLLLLLLLLLLLLLLLLLl",Toast.LENGTH_LONG).show()
            }
    }

}
