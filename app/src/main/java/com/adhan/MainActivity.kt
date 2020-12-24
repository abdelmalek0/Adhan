package com.adhan

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.adhan.language.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_prayer_times.*
import java.lang.Exception
import java.util.*


class MainActivity : BaseActivity(),MainInterface,HomeInterface {
    lateinit var mainFragment : Fragment
    private val MY_PERMISSIONS_REQUEST_LOCATION = 99
    private val MY_PERMISSIONS_REQUEST_COARSE = 100
    lateinit var sharedPreferences : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("Adhan", 0)
        val theme = sharedPreferences.getInt("theme",0)
        val lang = sharedPreferences.getString("language","Arabic")

        when(theme) {
            1 -> setTheme(R.style.GreenyTheme)
            2 -> setTheme(R.style.BrickTheme)
            else -> setTheme(R.style.AppTheme)
        }
        setContentView(R.layout.activity_main)
        if(lang == "Arabic"){
            bottom_navigation.layoutDirection = View.LAYOUT_DIRECTION_RTL
        }else{
            bottom_navigation.layoutDirection = View.LAYOUT_DIRECTION_LTR
        }
        init()
        checkLocationPermission()
        changeFragment()
        turnDataGetterOn()
    }


    override fun getTheme(): Resources.Theme {
        return super.getTheme()
    }
    private fun changeFragment(){
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> {
                    goToHomeFragment()
                    true
                }
                R.id.audio -> {
                    goToAudioFragment()
                    true
                }
                R.id.map -> {
                    goToMapFragment()
                    true
                }
                R.id.settings -> {
                    goToSettingsFragment()
                    true
                }
                else -> false
            }
        }
    }

    private fun init(){
        mainFragment = PrayerTimesFragment()
        supportFragmentManager.beginTransaction().add(R.id.mainFrame,mainFragment).commit()
    }


    private fun checkLocationPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED|| ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("R.string.title_location_permission")
                    .setMessage("text_location_permission")
                    .setPositiveButton("R.string.ok",
                        DialogInterface.OnClickListener { dialogInterface, i -> //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(
                                this@MainActivity,
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                MY_PERMISSIONS_REQUEST_LOCATION
                            )
                        })
                    .create()
                    .show()
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle("R.string.title_location_permission")
                    .setMessage("text_location_permission")
                    .setPositiveButton("R.string.ok",
                        DialogInterface.OnClickListener { dialogInterface, i -> //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(
                                this@MainActivity,
                                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                                MY_PERMISSIONS_REQUEST_LOCATION
                            )
                        })
                    .create()
                    .show()

            }
            if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    != PackageManager.PERMISSION_GRANTED){
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(
                        this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_REQUEST_LOCATION
                    )
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(
                        this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                        MY_PERMISSIONS_REQUEST_COARSE
                    )
                }
            false
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {


                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
            MY_PERMISSIONS_REQUEST_COARSE->{

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {


                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }
    }

    override fun goToAudioFragment() {
        mainFragment = AudioFragment()
        supportFragmentManager.beginTransaction().replace(R.id.mainFrame,mainFragment).commit()

    }

    override fun goHomeButtonChecked(){
        try {
            bottom_navigation.selectedItemId = R.id.home
        }catch (e:Exception){}
    }
    override fun goToHomeFragment() {
        mainFragment = PrayerTimesFragment()
        supportFragmentManager.beginTransaction().replace(R.id.mainFrame,mainFragment).commit()
        //goHomeButtonChecked()
    }

    override fun goToSettingsFragment() {
        mainFragment = SettingsFragment()
        supportFragmentManager.beginTransaction().replace(R.id.mainFrame,mainFragment).commit()

    }

    override fun goToMapFragment() {
        mainFragment = LocationFragment()
        supportFragmentManager.beginTransaction().replace(R.id.mainFrame,mainFragment).commit()

    }

    fun turnDataGetterOn(){
        val  shared = getSharedPreferences("notification", Context.MODE_PRIVATE)
        val bool = shared.getBoolean("notification",true)
        if(bool){
            val i = Intent(this, DataGetter::class.java)
            startService(i)
        }
    }
}