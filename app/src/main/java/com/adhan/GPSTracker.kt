package com.adhan

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class GPSTracker: Service() {
    private var locationManager: LocationManager? = null
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("hada","nice")
        locationManager = getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager?
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return START_NOT_STICKY
        }


        // Request location updates
        locationManager?.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            0L,
            0f,
            locationListener
        )

        return START_NOT_STICKY
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(p0: Location?) {
            Log.d("locationtag", "" + p0?.longitude + ":" + p0?.latitude)
            if (p0 != null) {
                sendMessageToActivity(p0,"now")
            }

            locationManager?.removeUpdates(this)
            stopSelf()
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private fun sendMessageToActivity(
        l: Location,
        msg: String
    ) {
        val intent = Intent("GPSLocationUpdates")
        // You can also include some extra data.
        intent.putExtra("Status", msg)
        val b = Bundle()
        b.putParcelable("Location", l)
        intent.putExtra("Location", b)
        LocalBroadcastManager.getInstance(baseContext).sendBroadcast(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}
