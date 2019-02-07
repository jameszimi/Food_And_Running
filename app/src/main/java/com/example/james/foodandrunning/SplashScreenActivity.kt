package com.example.james.foodandrunning

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //ซ่อนไตเติลบาร์
        window.requestFeature(Window.FEATURE_NO_TITLE)

        //ทำให้ Activety เป็นแบบ Fullsceen
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)

        dayfun()

        val dayList = ArrayList<Int>(dayfun())

        //โชว์ 3 วิ
        Handler().postDelayed({
            //Start Login
            startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
            finish()
        }, 3000)

    }
    fun dayfun(): ArrayList<Int> {
        val dayArrayList = ArrayList<Int>()
        for (i in 1..30){
            dayArrayList.add(i)
        //    println("i = $i")
        }
       // println("funday = $dayArrayList")
        return dayArrayList
    }

}
