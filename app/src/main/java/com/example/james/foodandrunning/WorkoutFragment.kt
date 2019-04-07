package com.example.james.foodandrunning


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
class WorkoutFragment : Fragment() {

    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_workout, container, false)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        //val mapFragment = fragmentManager!!.findFragmentById(R.id.workmap) as SupportMapFragment
       // mapFragment.getMapAsync(this)



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

    //override fun onMapReady(googleMap: GoogleMap) {
    //
    //        mMap = googleMap
    //
    //        // Add a marker in Sydney and move the camera
    //        val sydney = LatLng(-34.0, 151.0)
    //        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
    //        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    //
    //    }

}
