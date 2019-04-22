package com.example.james.foodandrunning

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.util.Log
import android.widget.Toast
import com.example.james.foodandrunning.setupdata.AppPreferences
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.main_toolbar.*

class LoginActivity : AppCompatActivity() {

    lateinit var toolbar: ActionBar
    val TAG = "LoginActivity App "
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //supportActionBar!!.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        setContentView(R.layout.activity_login)

        //set Title bar
        setSupportActionBar(findViewById(R.id.mtoolbar))
        toolbar = supportActionBar!!
        toolbar.title = "Login"

        //val actionBar = supportActionBar

        //actionBar!!.title = "Login"


        //Intent to RegisterActivity
        textlinktoregister.setOnClickListener {

            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))

        }

        //Intent to Login to MainActivity Activity
        LoginButton.setOnClickListener {

            loginPerform()
            //startActivity(Intent(this, MainActivity::class.java)) //for test not login

        }


    }

    private fun loginPerform() {

        val email = userNameInput.text.toString()
        val password = passwordInput.text.toString()

        if (email.isEmpty()) {
            Toast.makeText(this, "กรุณาใส่อีเมล์", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "กรุณา Password", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener {

            if (!it.isSuccessful){
                return@addOnCompleteListener
            }
            println(TAG+it.result!!.user.uid)
            AppPreferences(this).setPreferenceUID(it.result!!.user.uid)
            Log.d("LoginActivity", "Login Success")
            startActivity(Intent(this, MainActivity::class.java))

        }

    }
}
