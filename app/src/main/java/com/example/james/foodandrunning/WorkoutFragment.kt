package com.example.james.foodandrunning


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class WorkoutFragment : Fragment(), OnMapReadyCallback {


    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    var locationManager : LocationManager? =null
    var locationListener : LocationListener? =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_workout, container, false)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        //val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        //mapFragment.getMapAsync(this)

         mapFragment = childFragmentManager.findFragmentById(R.id.maprunning) as SupportMapFragment
         println("mapFragment : "+ (mapFragment != null))
         println("this = "+this)
         if (mapFragment != null) {
             mapFragment.getMapAsync(this)
             println("mapFragment not null")
         } else {
             val fm = fragmentManager
             val ft = fm!!.beginTransaction()
             mapFragment = SupportMapFragment.newInstance()
             ft.replace(R.id.maprunning, mapFragment).commit()
             println("mapFragment else")
         }



        val addcalimg = v.findViewById<ImageView>(R.id.addimg)
        addcalimg.setOnClickListener {
            val intent = Intent(activity, AddRunningActivity::class.java)
            startActivity(intent)
        }

        // Inflate the layout for this fragment
        return v
    }

    companion object {
        fun newInstance(): WorkoutFragment = WorkoutFragment()
    }

    override fun onMapReady(googleMap: GoogleMap) {

        var count = 0
        mMap = googleMap
        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val btn_runnig = activity?.findViewById<Button>(R.id.btn_startrunning)
        btn_runnig!!.visibility = View.VISIBLE
        btn_runnig!!.setOnClickListener {
            val intent = Intent(context,RunningActivity::class.java)
            startActivity(intent)
        }

        locationListener = object : LocationListener {






            override fun onLocationChanged(location: Location?) {

                if (location != null) {

                    println("map = latitude:${location.latitude} longitude:${location.longitude}")
                    mMap.clear()

                    val userLocation = LatLng(location.latitude,location.longitude)
                    mMap.addMarker(MarkerOptions().position(userLocation).title("You location"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,18.8f))
                    count += 1
                    println("count=$count")

                }


            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String?) {
            }

            override fun onProviderDisabled(provider: String?) {
            }

        }

        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)

        } else {
            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,2f,locationListener)
            val lastLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            var userLastLocation = LatLng(lastLocation.latitude,lastLocation.longitude)
            mMap.addMarker(MarkerOptions().position(userLastLocation).title("james location"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLastLocation,18.8f))
            println("count=$count")

        }

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == 1) {

            if (grantResults.size > 0) {
                if (ContextCompat.checkSelfPermission(context!!,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,2f,locationListener)
                }
            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}
