package com.example.james.foodandrunning

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_running.*

class RunningActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    var locationManager : LocationManager? =null
    var locationListener : LocationListener? =null
    var status = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_running)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.maprunning) as SupportMapFragment
        mapFragment.getMapAsync(this)

        var stoptime : Long = 0
        cockshow.base = SystemClock.elapsedRealtime()+stoptime
        cockshow.start()

        btn_pauserun.setOnClickListener {
            stoptime = cockshow.base-SystemClock.elapsedRealtime()
            println("time:$stoptime")
            cockshow.stop()
            btn_pauserun.visibility = View.GONE
            btn_resumerun.visibility = View.VISIBLE
            status = 0
        }

        btn_resumerun.setOnClickListener {
            stoptime = cockshow.base+SystemClock.elapsedRealtime()
            println("time:$stoptime")
            cockshow.start()
            btn_resumerun.visibility = View.GONE
            btn_pauserun.visibility = View.VISIBLE
            status = 1
        }

        btn_finishrun.setOnClickListener {
            cockshow.stop()
            val intent = Intent(this,ResultsRunningActivity::class.java)
            intent.putExtra("time",stoptime)
            startActivity(intent)
        }



    }

    override fun onMapReady(googleMap: GoogleMap) {
        var count = 0
        mMap = googleMap
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationListener = object : LocationListener {






            override fun onLocationChanged(location: Location?) {

                if (location != null) {

                    if (status == 1) {
                        println("map = latitude:${location.latitude} longitude:${location.longitude}")
                        mMap.clear()

                        val userLocation = LatLng(location.latitude,location.longitude)
                        mMap.addMarker(MarkerOptions().position(userLocation).title("You location"))
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,18.8f))
                        count += 1
                        println("count=$count")
                    } else {
                        println("Pause ++++++++++++++ ")
                    }

                }
                println("SSSSSSSSSSSSSSSSS $status")


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
            println("count=$count")

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == 1) {

            if (grantResults.size > 0) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,2f,locationListener)
                }
            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
