package com.example.pokemon

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.reflect.Constructor

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        Checkpermission()
    }
var ACCESLOCATION =123

    fun Checkpermission(){
        if(Build.VERSION.SDK_INT>=23){
            if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),ACCESLOCATION)
                return

            }
        }
        getUserLocation()
        LoadPokemon()
    }

    private fun getUserLocation() {
        Toast.makeText(this,"your location acces on",Toast.LENGTH_LONG).show()
        var myLocation = MylocationListener()
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3f,myLocation)
        var mythread = myThread()
        mythread.start()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode)   {
            ACCESLOCATION ->{
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    getUserLocation()
                }
                else{
                    Toast.makeText(this,"No acces your location",Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera

    }

var location:Location ?=null
var oldLocation:Location ?=null

    inner class MylocationListener:LocationListener{
        override fun onLocationChanged(provider: Location?) {
            location =provider
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderEnabled(provider: String?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderDisabled(provider: String?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        constructor() {
            location=Location("Start")
            location!!.longitude=0.0
            location!!.longitude=0.0

        }



    }

    inner class myThread:Thread{
        constructor():super(){
            oldLocation=Location("Start")
            oldLocation!!.longitude=0.0
            oldLocation!!.longitude=0.0
        }
        override fun run() {
            while (true ){
                try {

                    if (oldLocation!!.distanceTo(location)==0f){
                        continue
                    }
                    oldLocation=location

                    runOnUiThread(){
                        mMap!!.clear()

                        //draw me

                        val sydney = LatLng(location!!.latitude,location!!.longitude)
                        mMap!!.addMarker(MarkerOptions()
                            .position(sydney)
                            .title("Marker in Sydney")
                            .snippet("my locations")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario)))
                       // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,4f))

                        //draw pokemon
                        for (i in 0..listPokemon.size-1)
                        {
                            var newPokemon = listPokemon[i]
                            if (newPokemon.isCatch==false)
                            {
                                val pokemonLoc = LatLng(newPokemon.location!!.latitude,newPokemon.location!!.longitude)
                                mMap!!.addMarker(MarkerOptions()
                                    .position(pokemonLoc)
                                    .title(newPokemon.name)
                                    .snippet(newPokemon.des+" ,power" +newPokemon.power )
                                    .icon(BitmapDescriptorFactory.fromResource(newPokemon.image!!)))

                                    if(location!!.distanceTo(newPokemon.location)<2){
                                        newPokemon.isCatch=true
                                        listPokemon[i]=newPokemon
                                        playerPower+= newPokemon.power!!
                                        Toast.makeText(applicationContext,"you catch your pokemon , your power "+playerPower,Toast.LENGTH_LONG).show()
                                    }
                            }

                        }



                    }
                    Thread.sleep(1000)
                }catch (ex:Exception){}
            }
        }
    }

 var playerPower = 0.0
var listPokemon=ArrayList<Pokemon>()
    fun LoadPokemon(){
        listPokemon.add(Pokemon(R.drawable.bulbasaur,
            "bulbasaur","bulbasaur from japan",200.0,10.8428,106.8287))
        listPokemon.add(Pokemon(R.drawable.charmander,
            "charmander","charmander from japan",100.0,10.7873,106.7498))
        listPokemon.add(Pokemon(R.drawable.squirtle,
            "squirtle","squirtle from japan",50.0,10.7757,106.7004))

    }
}
