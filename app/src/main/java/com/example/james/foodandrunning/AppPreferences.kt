package com.example.james.foodandrunning

import android.content.Context

class AppPreferences(context : Context) {

    val PREFERENCE_NAME = "FoodandRunning"
    val PREFERENCE_UID = "PreferenceUID"
    val PREFERENCE_COUNT = "LoginCount"

    val preferences = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)

    fun getPreferenceUID() : String {
        return preferences.getString(PREFERENCE_UID,"")
    }

    fun setPreferenceUID(uid : String) {
        val editor = preferences.edit()
        editor.putString(PREFERENCE_UID,uid)
        editor.apply()
    }

    fun getLoginCount() : Int {
        return preferences.getInt(PREFERENCE_COUNT,0)
    }

    fun setLoginCount(count:Int) {
        val  editor = preferences.edit()
        editor.putInt(PREFERENCE_COUNT,count)
        editor.apply()
    }
}