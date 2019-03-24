package com.example.james.foodandrunning

import android.content.Context

class AppPreferences(context : Context) {

    val PREFERENCE_NAME = "FoodandRunning"
    val PREFERENCE_UID = "PreferenceUID"
    val PREFERENCE_AGE = "PreferenceAge"
    val PREFERENCE_COUNT = "PreferenceCount"
    val PREFERENCE_SEX = "PreferenceSex"
    val PREFERENCE_WEIGHT = "PreferenceWeight"
    val PREFERENCE_HEIGHT = "PreferenceHeight"
    val PREFERENCE_STATUS = "PreferenceStatus"
    val PREFERENCE_ROUTINE = "PreferenceRoutine"
    val PREFERENCE_CAL = "PreferenceCal"
    val PREFERENCE_BFCAL = "PreferenceBFCal"
    val PREFERENCE_LUCAL = "PreferenceLUCal"
    val PREFERENCE_DNCAL = "PreferenceDNCal"
    val PREFERENCE_SNCAL = "PreferenceSNCal"
    val PREFERENCE_TOTALEAT = "PreferenceTotalEat"

    val preferences = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)

    fun getPreferenceUID() : String {
        return preferences.getString(PREFERENCE_UID,"")
    }

    fun setPreferenceUID(uid : String) {
        val editor = preferences.edit()
        editor.putString(PREFERENCE_UID,uid)
        editor.apply()
    }

    fun getPreferenceAge() : Int {
        return preferences.getInt(PREFERENCE_AGE,0)
    }

    fun setPreferenceAge(age : Int) {
        val editor = preferences.edit()
        editor.putInt(PREFERENCE_AGE,age)
        editor.apply()
    }

    fun getPreferenceCount() : Int {
        return preferences.getInt(PREFERENCE_COUNT,0)
    }

    fun setPreferenceCount(count : Int) {
        val editor = preferences.edit()
        editor.putInt(PREFERENCE_COUNT,count)
        editor.apply()
    }

    fun getPreferenceSex() : Int {
        return preferences.getInt(PREFERENCE_SEX,0)
    }

    fun setPreferenceSex(sex : Int) {
        val editor = preferences.edit()
        editor.putInt(PREFERENCE_SEX,sex)
        editor.apply()
    }

    fun getPreferenceWeight() : Int {
        return preferences.getInt(PREFERENCE_WEIGHT,0)
    }

    fun setPreferenceWeight(weight : Int) {
        val editor = preferences.edit()
        editor.putInt(PREFERENCE_WEIGHT,weight)
        editor.apply()
    }

    fun getPreferenceHeight() : Int {
        return preferences.getInt(PREFERENCE_HEIGHT,0)
    }

    fun setPreferenceHeight(height : Int) {
        val editor = preferences.edit()
        editor.putInt(PREFERENCE_HEIGHT,height)
        editor.apply()
    }

    fun getPreferenceStatus() : Int {
        return preferences.getInt(PREFERENCE_STATUS,0)
    }

    fun setPreferenceStatus(status : Int) {
        val editor = preferences.edit()
        editor.putInt(PREFERENCE_STATUS,status)
        editor.apply()
    }

    fun getPreferenceRoutine() : Int {
        return preferences.getInt(PREFERENCE_ROUTINE,0)
    }

    fun setPreferenceRoutine(routine : Int) {
        val editor = preferences.edit()
        editor.putInt(PREFERENCE_ROUTINE,routine)
        editor.apply()
    }

    fun getPreferenceCal() : Int {
        return preferences.getInt(PREFERENCE_CAL,0)
    }

    fun setPreferenceCal(cal : Int) {
        val editor = preferences.edit()
        editor.putInt(PREFERENCE_CAL,cal)
        editor.apply()
    }

    fun getPreferenceBFCal() : Int {
        return preferences.getInt(PREFERENCE_BFCAL,0)
    }

    fun setPreferenceBFCal(cal : Int) {
        val editor = preferences.edit()
        editor.putInt(PREFERENCE_BFCAL,cal)
        editor.apply()
    }

    fun getPreferenceLUCal() : Int {
        return preferences.getInt(PREFERENCE_LUCAL,0)
    }

    fun setPreferenceLUCal(cal : Int) {
        val editor = preferences.edit()
        editor.putInt(PREFERENCE_LUCAL,cal)
        editor.apply()
    }

    fun getPreferenceDNCal() : Int {
        return preferences.getInt(PREFERENCE_DNCAL,0)
    }

    fun setPreferenceDNCal(cal : Int) {
        val editor = preferences.edit()
        editor.putInt(PREFERENCE_DNCAL,cal)
        editor.apply()
    }

    fun getPreferenceSNCal() : Int {
        return preferences.getInt(PREFERENCE_SNCAL,0)
    }

    fun setPreferenceSNCal(cal : Int) {
        val editor = preferences.edit()
        editor.putInt(PREFERENCE_SNCAL,cal)
        editor.apply()
    }

    fun getPreferenceTotalEat() : Int {
        return preferences.getInt(PREFERENCE_TOTALEAT,0)
    }

    fun setPreferenceTotalEat(cal : Int) {
        val editor = preferences.edit()
        editor.putInt(PREFERENCE_TOTALEAT,cal)
        editor.apply()
    }

}