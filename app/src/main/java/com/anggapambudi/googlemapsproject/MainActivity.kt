package com.anggapambudi.googlemapsproject

import android.location.Location
import android.os.Bundle
import android.util.Log
import com.anggapambudi.googlemapsproject.databinding.ActivityMainBinding
import com.crocodic.core.base.activity.NoViewModelActivity
import com.crocodic.core.extension.checkLocationPermission
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : NoViewModelActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutRes(R.layout.activity_main)
        binding.mapView.onCreate(savedInstanceState)

        //location permission
        checkLocationPermission {
            listenLocationChange()
        }

    }

    override fun retrieveLocationChange(location: Location) {
        Log.d("lokasi device", "latitude: ${location.latitude} longitude: ${location.longitude}")

        //set marker
        binding.mapView.getMapAsync {

            val myLocation = LatLng(location.latitude, location.longitude)
            it.addMarker(MarkerOptions()
                .position(myLocation)
                .title("Lokasi Saya"))
            it.moveCamera(CameraUpdateFactory.newLatLng(myLocation))

        }

    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}