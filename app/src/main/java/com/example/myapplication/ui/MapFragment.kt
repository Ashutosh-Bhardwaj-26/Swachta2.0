package com.example.myapplication.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.myapplication.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MapMain.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapFragment : Fragment() , OnMapReadyCallback ,GoogleMap.OnMarkerClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var lastlocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mapFragment : SupportMapFragment
    private lateinit var mMap: GoogleMap

    private lateinit var database: DatabaseReference
    private lateinit var database2: DatabaseReference
    private lateinit var database3: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_map_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        database = FirebaseDatabase.getInstance().getReference("CurbsideUID")
        database2 = FirebaseDatabase.getInstance().getReference("CurbsideUserLocation")
        database3 = FirebaseDatabase.getInstance().getReference("User")


        mapFragment = childFragmentManager.findFragmentById(R.id.map2) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override  fun onMapReady(googleMap: GoogleMap){
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        setUpMAP()

    }

    private fun setUpMAP() {




        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf( Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE)
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->


            if(location !=null){
                lastlocation = location
                val currentLatLong = LatLng(location.latitude,location.longitude)
                placeMarkerOnMap(currentLatLong)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong,12f))
            }

        }
    }

    private fun refreshActivity() {
        val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false)
            Toast.makeText(this.activity, "Successfully LoggedIn", Toast.LENGTH_SHORT).show()
        }
        ft.detach(this).attach(this).commit()
    }

    private fun placeMarkerOnMap(currentLatLong: LatLng) {

        var loop = 0
        lateinit var userID: String
        var latitude: String = currentLatLong.latitude.toString()
        var longitude: String = currentLatLong.longitude.toString()
        var name: String = "Charlie"
        var locationList: Map<String,String>

        database.child("curbCount").get().addOnSuccessListener{
            loop = it.value.toString().toInt()
        }

        for(i in 0..loop){
            database.child(i.toString()).get().addOnSuccessListener {
                userID = it.value.toString()

                database2.child(userID).get().addOnSuccessListener{
                    if(it.child("latitude").value.toString() != "null"){
                        latitude = it.child("latitude").value.toString()
                        longitude = it.child("longitude").value.toString()
                        database3.child(userID).get().addOnSuccessListener {
                            name = it.child("name").value.toString()

                            val curbLatLong = LatLng(latitude.toDouble(),longitude.toDouble())
                            val markerOptionsp = MarkerOptions().position(curbLatLong)
                            markerOptionsp.title(name)
                            mMap.addMarker(markerOptionsp)

                        }.addOnFailureListener{}
                    }
                }.addOnFailureListener{}
            }.addOnFailureListener{}
        }



//        val markerOptions = MarkerOptions().position(currentLatLong)
//        markerOptions.title("$currentLatLong")
//        mMap.addMarker(markerOptions)
    }
    override fun onMarkerClick(p0: Marker) = false

    companion object {

        const val LOCATION_REQUEST_CODE = 1

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MapMain.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}
