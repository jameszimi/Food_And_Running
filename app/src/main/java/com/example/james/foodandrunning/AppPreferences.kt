package com.example.james.foodandrunning

import android.content.Context

class AppPreferences(context : Context) {

    val PREFERENCE_NAME = "FoodandRunning"
    val PREFERENCE_UID = "PreferenceUID"

    val preferences = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)

    fun getPreferenceUID() : String {
        return preferences.getString(PREFERENCE_UID,"")
    }

    fun setPreferenceUID(uid : String) {
        val editor = preferences.edit()
        editor.putString(PREFERENCE_UID,uid)
        editor.apply()
    }

}