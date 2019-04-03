package com.example.james.foodandrunning.setupdata

import java.util.*

data class FoodTotalCal(val name : String, val value: Float){

}

data class FoodNCal(val foodName : String, val totalCal : Int, val foodConsume : String) {

}

data class WeightId(val dateWeight : Long, val weightValue : Int) {

}

data class Userfood(val foodName: String, val totalCal: Int) {

}

class ExampleItem(line1:String, line2:String) {
    val mline1 = line1
    val mline2 = line2
}

