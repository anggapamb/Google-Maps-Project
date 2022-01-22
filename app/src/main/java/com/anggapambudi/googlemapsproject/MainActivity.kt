package com.anggapambudi.googlemapsproject

import android.location.Location
import android.os.Bundle
import android.util.Log
import com.anggapambudi.googlemapsproject.data.MyMessage
import com.anggapambudi.googlemapsproject.databinding.ActivityMainBinding
import com.crocodic.core.base.activity.NoViewModelActivity
import com.crocodic.core.extension.checkLocationPermission
import com.crocodic.core.extension.popNotification
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.maps.route.extensions.drawRouteOnMap
import com.maps.route.extensions.moveCameraOnMap
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : NoViewModelActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutRes(R.layout.activity_main)
        binding.mapView.onCreate(savedInstanceState)

        //location permission use crocodic core
        checkLocationPermission { listenLocationChange() }

        //AutoComplete
        autoCompleteFragment()

        //maps route
        binding.mapView.getMapAsync {

            //latitude & longitude
            val hermina = LatLng(-7.0727956, 110.3766561)
            val crocodic = LatLng(-7.0643998, 110.4143387)

            it.moveCameraOnMap(latLng = crocodic)

            //
            it.drawRouteOnMap(
                "AIzaSyDMF-5SBSgr8ynHOQtKj7kZLHLwuTc1aoA",
                source = crocodic,
                destination = hermina,
                context = applicationContext
            )

        }

        //test pop notification
        binding.btnTestNotification.setOnClickListener {
            //popNotification("Pesan baru", "haloo test test")
            val newMessage = MyMessage("Pesan baru", "udah makan ?")
            EventBus.getDefault().post(newMessage)

        }


    }

    private fun autoCompleteFragment() {
        //SDK
        Places.initialize(applicationContext, "AIzaSyDMF-5SBSgr8ynHOQtKj7kZLHLwuTc1aoA")
        //
        val autoCompleteFragment = supportFragmentManager.findFragmentById(R.id.fragment_autocomplete) as AutocompleteSupportFragment
        //
        autoCompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))
        //
        autoCompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener{
            override fun onError(p0: Status) {
                Log.i("latihan_autocomplete", "An error occurred: $p0")
            }

            override fun onPlaceSelected(p0: Place) {
                Log.i("latihan_autocomplete", "Place: ${p0.name}, ${p0.id}")
            }

        })

    }

    override fun retrieveLocationChange(location: Location) {
        Log.d("lokasi device", "latitude: ${location.latitude} longitude: ${location.longitude}")

        //set marker
//        binding.mapView.getMapAsync {
//
//            val myLocation = LatLng(location.latitude, location.longitude)
//            it.addMarker(MarkerOptions()
//                .position(myLocation)
//                .title("Lokasi Saya"))
//
//        }

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showMessage(myMessage: MyMessage) {
        popNotification(myMessage.title, myMessage.content)
    }
}