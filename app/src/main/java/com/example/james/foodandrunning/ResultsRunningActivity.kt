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
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.example.james.foodandrunning.setupdata.Runningpath
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.activity_resultsrunning.*

class ResultsRunningActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var locationManager : LocationManager? =null
    var locationListener : LocationListener? =null
    var distancePath = ArrayList<LatLng>()
    var listtoupdate = ArrayList<Runningpath>()


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mMap.addPolyline(
            PolylineOptions()
                .addAll(distancePath)
                .width(7f)
                .color(Color.RED))

        locationListener = object : LocationListener {

            override fun onLocationChanged(location: Location?) {
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
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLastLocation,18.8f))

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultsrunning)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapfinish) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val builder = intent
        distancePath = builder.getSerializableExtra("listmap") as ArrayList<LatLng>

        val calories = builder.getStringExtra("calories").toDouble()
        val stoptime = builder.getStringExtra("stoptime")
        val distance = builder.getStringExtra("distance").toDouble()
        cockfinish.text = stoptime

        println("ResultsRunningActivity listmap:"+distancePath)
        println("ResultsRunningActivity listtoupdate:"+listtoupdate)
        println("ResultsRunningActivity calories:"+calories)
        println("ResultsRunningActivity stoptime:"+stoptime)
        println("ResultsRunningActivity distance:$distance")

        caloriesText.text = String.format("%.2f",calories)
        distanceText.text = String.format("%.2f",distance)

        endBtn.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }


    }
}
