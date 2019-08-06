package com.example.james.foodandrunning


import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout


class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"
    lateinit var toolbar : ActionBar
    lateinit var mMainFrame : FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mMainFrame = findViewById(R.id.main_frame)

        setSupportActionBar(findViewById(R.id.mtoolbar))
        toolbar = supportActionBar!!
        toolbar.title = "บันทึกรายการอาหาร"

        val calorieFragment = CalorieFragment.newInstance()
        openFragment(calorieFragment)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.main_nav)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_calorie -> {
                toolbar.title = "บันทึกรายการอาหาร"
                val calorieFragment = CalorieFragment.newInstance()
                openFragment(calorieFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_workout -> {
                toolbar.title = "ออกกำลังกาย"
                val workoutFragment = WorkoutFragment.newInstance()
                openFragment(workoutFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_user -> {
                toolbar.title = "ข้อมูลผู้ใช้"
                val userFragment = UserFragment.newInstance()
                openFragment(userFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    //ทำการเรียกหน้า fragment
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_frame, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
