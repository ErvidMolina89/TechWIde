package com.widetech.mobile.wide_tech.ui.dashboard

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.widetech.mobile.wide_tech.Base.App
import com.widetech.mobile.wide_tech.Base.BaseFragment
import com.widetech.mobile.wide_tech.R
import com.widetech.mobile.wide_tech.Utils.GPSTracker
import com.widetech.mobile.wide_tech.Utils.SettingPermision
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class ContactsFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var dashboardViewModel: ContactsViewModel
    var marker: Marker? = null
    var position: Location? = null
    var geoCoder: Geocoder? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(ContactsViewModel::class.java)
        vista = inflater.inflate(R.layout.fragment_contacts, container, false)
        val GpsTracker = GPSTracker(activity)
        position = GpsTracker.location
        geoCoder = Geocoder(context, Locale.getDefault())
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mapView: MapView?
        super.onViewCreated(view, savedInstanceState)
        mapView = vista!!.findViewById(R.id.map_contact) as MapView
        mapView.onCreate(null)
        mapView.onResume()
        mapView.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        checarSerialGPS()
    }

    private fun validatePermission(){
        if ((ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            SettingPermision(App.mContext!!, baseActivity!!)
            return
        }
        PosicionActual()
    }

    fun checarSerialGPS(){
        try {
            val gps = Settings.Secure.getInt(baseActivity!!.contentResolver, Settings.Secure.LOCATION_MODE)

            if (gps == 0){
                Toast.makeText(baseActivity, "Por favor , para continuar, habilita la señal GPS", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }catch (e: Settings.SettingNotFoundException){}
    }

    fun PosicionActual() {
        if (position == null) {
            Log.e("Location", "Esta null")
            return
        }
        if (marker == null){
            val markerOptions = MarkerOptions()
            markerOptions.position(LatLng(position!!.latitude, position!!.longitude))
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker))
            markerOptions.draggable(true)
            this.marker = mMap.addMarker(markerOptions)
        }else{
            marker!!.position = LatLng(position!!.latitude, position!!.longitude)
            //marcador!!.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marcador))
        }
        marcadorSnipet()
        camaraZoom(zoom = 14f)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        geoCoder = Geocoder(context, Locale.getDefault())
        marcadorGps()
        aparienciaMap()
        validatePermission()
    }

    private fun marcadorGps() {
        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener{

            // Para que al final de arrastrar el marcador muestre la ciudad y pais y otros datos
            override fun onMarkerDragEnd(marker: Marker) {
                marcadorSnipet()
            }

            override fun onMarkerDragStart(pO: Marker) {
                //para quitar del marcador el snippet
                pO.hideInfoWindow()
            }

            override fun onMarkerDrag(pO: Marker?) {

            }
        })
    }

    fun aparienciaMap(){
        try {
            val success: Boolean = mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(this@ContactsFragment.activity, R.raw.maps_style)
            )

            if (!success){
                Log.e("","Style parent failed")
            }
        }catch (e: Exception){
            Log.e("","Can't find the file")
        }
    }

    fun camaraZoom(zoom: Float){

        val camara = CameraPosition.Builder()
            .target(LatLng(position!!.latitude, position!!.longitude))
            .zoom(zoom)
            //rotación 0-360º
            .bearing(0f)
            //angulo 0-90º
            .tilt(0f)
            .build()
        //
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(camara))
    }

    fun marcadorSnipet(){

        var direccion: List<Address>? = null
        val latitud: Double = position!!.latitude
        val longitud: Double = position!!.longitude

        try {
            direccion = this@ContactsFragment.geoCoder!!.getFromLocation(latitud, longitud, 1)

        }catch (e: java.lang.Exception){
            Log.e("error1", Log.getStackTraceString(e))
        }
    }

    fun Prueba(){
        GlobalScope.launch {
            delay(1000)
        }
    }
}