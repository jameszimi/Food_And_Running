package com.example.james.foodandrunning

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.util.Log
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_user_editdata.*

class UserEditdataActivity : AppCompatActivity() {

    lateinit var toolbar : ActionBar
    val dummyuid = "4JXuStGGvTfxbJe3anMC2glJNEZ2"
    val TAG = "UserEdirdataActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_editdata)

        //set toolbar
        setSupportActionBar(findViewById(R.id.mtoolbar))
        toolbar = supportActionBar!!
        toolbar.title = "แก้ไขข้อมูลสมาชิก"
        toolbar.setDisplayHomeAsUpEnabled(true)


        val db = FirebaseFirestore.getInstance()

        //Create a reference to get document in collection
        val usererf = db.collection("MEMBER_TABLE").document(dummyuid)

        //Create a query against the collection
        usererf.get().addOnSuccessListener {
            if (it != null) {
                val dataHash = it.data as HashMap<String, Any>
                member_name.text = (dataHash["member_name"].toString())
                member_username.text = (dataHash["member_username"].toString())
                member_password.text = (dataHash["member_password"].toString())
                member_height.text = (dataHash["member_height"].toString())
                member_diaryroutine.text = (dataHash["member_diaryroutine"].toString())
                member_email.text = (dataHash["member_email"].toString())
                member_birthday.text = birthfun(dataHash["member_birthday"].toString())

                Log.d(TAG, it.data.toString())
            } else {
                Log.d(TAG,"ไม่มีข้อมูล")
            }
        }
            .addOnFailureListener {
                Log.d(TAG,"Fail")
            }

    }

    private fun birthfun(birthday: String): String {

        return "ssss"

    }
}
