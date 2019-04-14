package com.example.james.foodandrunning

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Toast
import com.example.james.foodandrunning.firebase.auth.FirestoreRunnigAuth
import com.example.james.foodandrunning.setupdata.AppPreferences
import com.example.james.foodandrunning.setupdata.Runningpath
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.activity_running.*
import kotlin.math.absoluteValue

class RunningActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var locationManager : LocationManager? =null
    var locationListener : LocationListener? =null
    var oldLocation = Location(LocationManager.GPS_PROVIDER)
    var newLocation = Location(LocationManager.GPS_PROVIDER)
    var distance : Float = 0.0f
    var sumdistance = 0.0
    var distancePath = ArrayList<LatLng>()
    var calories = 0.00
    var pauseOfTime : Long = 0
    var stoptime : Long = 0
    var timeperdistance : Double = 0.0
    var locationOldTime : Double = 0.0
    var locationNewTime : Double = 0.0
    var status = false
    var indexinmap = 0
    var listtoupdate = ArrayList<Runningpath>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_running)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.maprunning) as SupportMapFragment
        mapFragment.getMapAsync(this)

        cockshow.base = SystemClock.elapsedRealtime()
        cockshow.start()
        status = true

        btn_pauserun.setOnClickListener {
            cockshow.stop()
            pauseOfTime = SystemClock.elapsedRealtime()-cockshow.base
            println("time:$stoptime btn_pauserun")
            btn_pauserun.visibility = View.GONE
            btn_resumerun.visibility = View.VISIBLE
            status = false
        }

        btn_resumerun.setOnClickListener {
            cockshow.base = SystemClock.elapsedRealtime()-pauseOfTime
            println("time:$stoptime btn_resumerun")
            cockshow.start()
            btn_resumerun.visibility = View.GONE
            btn_pauserun.visibility = View.VISIBLE
            status = true
        }

        btn_finishrun.setOnClickListener {
            cockshow.stop()
            stoptime = SystemClock.elapsedRealtime()-cockshow.base
            println("Stoptime:$stoptime distance:$distance")
            val showtime = cockshow.text

            FirestoreRunnigAuth(this).runningPathtoFirestore(distancePath,showtime.toString(),calories,sumdistance)

        }
        
    }

    override fun onMapReady(googleMap: GoogleMap) {
        var count = 0
        mMap = googleMap
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationListener = object : LocationListener {

            override fun onLocationChanged(location: Location?) {

                if (location != null) {

                    if (status){
                        println("status TRUE")
                        println("map = latitude:${location.latitude} longitude:${location.longitude}")
                        //mMap.clear()
                        oldLocation.set(newLocation)
                        newLocation.set(location)
                        if (newLocation.distanceTo(oldLocation) < 10000) distance = newLocation.distanceTo(oldLocation)

                        sumdistance += distance



                        calculateCalfun(distance)



                        val userLocation = LatLng(location.latitude,location.longitude)
                        //mMap.addMarker(MarkerOptions().position(userLocation).title("You location"))
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,19f))
                        count += 1
                        println("count=$count")

                        indexinmap += 1
                        distancePath.add(LatLng(location.latitude,location.longitude))
                        mMap.addPolyline(
                            PolylineOptions()
                                .addAll(distancePath)
                                .width(7f)
                                .color(Color.RED)
                        )
                    } else {
                        println("status FALSE")
                    }

                }

            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String?) {
            }

            override fun onProviderDisabled(provider: String?) {
            }

        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)

        } else {
            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,2f,locationListener)
            val lastLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val userLastLocation = LatLng(lastLocation.latitude,lastLocation.longitude)
            mMap.addMarker(MarkerOptions().position(userLastLocation).title("james location"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLastLocation,18.8f))

        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == 1) {

            if (grantResults.isNotEmpty()) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,2f,locationListener)
                }
            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun calculateCalfun(distance: Float) {

        locationOldTime = locationNewTime
        locationNewTime = (SystemClock.elapsedRealtime()-cockshow.base)-locationOldTime

        timeperdistance = (locationNewTime-locationOldTime).absoluteValue

        val weight = AppPreferences(this).getPreferenceWeight()
        var time = 0.000000000
        time = (timeperdistance/60000)
        println("TIME/60000 = "+timeperdistance/60000 + "time=$time")
        var totalCalories = 0.00
        var distanceMiles = 0.00
        var cheackcal = 0.00
        var mets = 0.0

        println("calculateCalfun = distance:$distance, timeperdistance:$timeperdistance, weight:$weight, time:$time")

        if (distance > 0) distanceMiles = distance*0.000621371192 //metes to miles

        if (time > 0 && distance > 0){
            cheackcal = (distanceMiles/time) // mile per min


            val calinsixteenmin = cheackcal*60 // mile per hr

            when {
                calinsixteenmin < 4 -> mets = 5.0
                calinsixteenmin <= 5 -> mets = 8.3
                calinsixteenmin <= 5.2 -> mets = 9.0
                calinsixteenmin <= 6 -> mets = 9.8
                calinsixteenmin <= 6.7 -> mets = 10.5
                calinsixteenmin <= 7 -> mets = 11.0
                calinsixteenmin <= 7.5 -> mets = 11.5
                calinsixteenmin <= 8 -> mets = 11.8
                calinsixteenmin <= 8.6 -> mets = 12.3
                calinsixteenmin <= 9 -> mets = 12.8
                calinsixteenmin <= 10 -> mets = 14.5
                calinsixteenmin <= 11 -> mets = 16.0
                calinsixteenmin <= 12 -> mets = 19.0
                calinsixteenmin <= 13 -> mets = 19.8
                calinsixteenmin <= 14 -> mets = 23.0
                else -> Toast.makeText(this,"สัญญาณขาดหาย", Toast.LENGTH_SHORT).show()
            }

            //แคลลอรี (นาที) = (MET x น้ำหนัก (kg) x 3.5) / 200
            if (distancePath.size > 0){

                println("testnow index:${indexinmap}")
                println("testnow value latitude:"+distancePath[distancePath.lastIndex].latitude+", longitude:"+distancePath[distancePath.lastIndex].longitude)


                totalCalories = ((mets*weight*3.5)/200)*time

                calories += totalCalories

                println("testnow calories:$calories")

                listtoupdate.add(Runningpath(distancePath[distancePath.lastIndex].latitude,distancePath[distancePath.lastIndex].longitude,distance.toDouble(),totalCalories))


            }

        }
    }
}
