package com.example.james.foodandrunning

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.james.foodandrunning.setupdata.AppPreferences
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var toolbar: ActionBar
    val TAG = "LoginActivity App "
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        //set Title bar
        setSupportActionBar(findViewById(R.id.mtoolbar))
        toolbar = supportActionBar!!
        toolbar.title = "Login"

        //Intent to RegisterActivity
        textlinktoregister.setOnClickListener {

            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))

        }

        //Intent to Login to MainActivity Activity
        LoginButton.setOnClickListener {
            loginPerform()
        }


    }

    private fun loginPerform() {

        val email = userNameInput.text.toString()
        val password = passwordInput.text.toString()

        if (email.isEmpty()) {
            return Toast.makeText(this, "กรุณาใส่อีเมล์", Toast.LENGTH_SHORT).show()
        }

        if (password.isEmpty()) {
            return Toast.makeText(this, "กรุณา Password", Toast.LENGTH_SHORT).show()
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener {

            if (!it.isSuccessful){
                return@addOnCompleteListener
            }
            AppPreferences(this).setPreferenceUID(it.result!!.user.uid)
            Log.d("LoginActivity", "Login Success")
            startActivity(Intent(this, MainActivity::class.java))

        }

    }
}
