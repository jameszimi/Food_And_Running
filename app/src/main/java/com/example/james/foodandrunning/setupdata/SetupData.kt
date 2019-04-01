package com.example.james.foodandrunning.setupdata

import java.util.*

data class FoodTotalCal(val name : String, val value: Float){

}

data class FoodNCal(val foodName : String, val totalCal : Int, val foodConsume : String) {

}

data class WeightId(val dateWeight : Long, val weightValue : Int) {

}