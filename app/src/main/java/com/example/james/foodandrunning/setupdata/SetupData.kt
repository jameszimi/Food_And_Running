package com.example.james.foodandrunning.setupdata

data class FoodTotalCal(val name : String, val value: Float){

}

data class FoodNCal(val foodName : String, val totalCal : Int, val foodConsume : String) {

}

data class WeightId(val dateWeight : Long, val weightValue : Int) {

}

data class Userfood(val foodName: String, val totalCal: Int) {

}

data class Runningpath(val latitude: Double, val longitude: Double, val distance: Double, val calories: Double){

}

data class RunningDataList(val day : Any?, val distance: Any?, val calories: Any?, val runningId : String){
}

data class FoodDetial(val food_id: String, val food_nameth: String){

}

data class FoodName(val foodName: String, val food_id: String){

}

