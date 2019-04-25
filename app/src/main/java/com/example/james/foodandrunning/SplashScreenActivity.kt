package com.example.james.foodandrunning

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import com.example.james.foodandrunning.setupdata.AppPreferences
import com.google.firebase.firestore.FirebaseFirestore

class SplashScreenActivity : AppCompatActivity() {

    private val TAG = "SplashScreenActivity "
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //ซ่อนไตเติลบาร์
        window.requestFeature(Window.FEATURE_NO_TITLE)

        //ทำให้ Activety เป็นแบบ Fullsceen
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)


        val appPreferences = AppPreferences(this)
        val loginUID = appPreferences.getPreferenceUID()

        if (loginUID.isNotEmpty()) {
            FirebaseFirestore.getInstance().collection("MEMBER_TABLE").document(loginUID).get().addOnSuccessListener {

                println(TAG+"data"+it.data)
                if (it.data != null) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    //โชว์ 3 วิ
                    Handler().postDelayed({
                        //Start Login
                        startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                        finish()
                    }, 3000)
                    //3000 is defuait
                }
            }
        } else {
            Handler().postDelayed({
                //Start Login
                startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                finish()
            }, 3500)
        }
        println(TAG + loginUID)


    }

}
