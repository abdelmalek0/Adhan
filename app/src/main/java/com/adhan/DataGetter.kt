package com.adhan

import android.app.Service
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.*
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.IBinder
import android.os.Parcelable
import android.os.PersistableBundle
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.adhan.cache.AppDatabase
import com.adhan.cache.Prayer
import com.adhan.cache.PrayerDao
import com.adhan.model.Day
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class DataGetter : Service() {
    // file pour ajouter les requetes http
    lateinit var mRequestQueue: RequestQueue
    lateinit var databaseDao : PrayerDao
    var url = ""
    var listPrayers : List<Prayer> = ArrayList<Prayer>()


    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    override fun onCreate() {
        super.onCreate()
        databaseDao = AppDatabase.getInstance(baseContext!!).prayerDao()
        val i = Intent(baseContext, GPSTracker::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        baseContext.startService(i)
        LocalBroadcastManager.getInstance(baseContext).registerReceiver(
            mMessageReceiver, IntentFilter("GPSLocationUpdates")
        )
        CoroutineScope(Dispatchers.IO).launch {
            listPrayers = databaseDao.getAll()
            withContext(Main){
                startJob()
            }
        }

    }

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            // Get extra data included in the Intent
            val message = intent.getStringExtra("Status")
            val b = intent.getBundleExtra("Location")
            var lastKnownLoc = b.getParcelable<Parcelable>("Location") as Location?
            if (lastKnownLoc != null) {
                Log.d("hada",lastKnownLoc.latitude.toString())
                Log.d("hada",lastKnownLoc.longitude.toString())
                var gcd: Geocoder = Geocoder(applicationContext, Locale("en"))
                try {
                    var addresses: List<Address> =
                        gcd.getFromLocation(lastKnownLoc?.latitude!!, lastKnownLoc?.longitude!!, 1)
                    if (addresses.isNotEmpty()) {
                        var city = addresses[0].locality.split(" ").toTypedArray()
                            .joinToString(separator = "%20")
                        Log.d("hada", city)
                        url =
                            "http://api.aladhan.com/v1/timingsByCity?city=$city&country=Algeria&method=3"
                        getData()

                    }
                }catch (e:Exception){}


            }
        }
    }
    private fun getBundle(): PersistableBundle{
        var b = PersistableBundle()
        var that = ArrayList<String>()
        for (element in listPrayers){
            val g = Gson()
            val json = g.toJson(element)
            that.add(json)
        }

        b.putStringArray("salawat",that.toTypedArray())
        return b
    }
    private fun startJob(){
        val componentName = ComponentName(this, NotificationService::class.java)
        val info = JobInfo.Builder(123,componentName)
            .setPersisted(true).setExtras(getBundle())
            .setPeriodic(15*60*1000).build()
        val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        if(scheduler.schedule(info) == JobScheduler.RESULT_SUCCESS){
            Log.d("scd","Job scheduled")
        } else {
            Log.d("scd","NOT")
        }
    }
    fun stopJob(){
        val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.cancel(123)
        Log.d("scd","Job cancelled")
    }



    fun getData(){

        mRequestQueue = Volley.newRequestQueue(baseContext)


        val mJsonObjectRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener { response ->

                try
                {
                    var obj = JSONObject(response)
                    var data: JSONObject = obj.getJSONObject("data")
                    var gson = Gson()
                    var jsonObject = data.getJSONObject("timings")
                    var day = gson.fromJson(jsonObject.toString(), Day::class.java)
                    CoroutineScope(Dispatchers.IO).launch {
                        databaseDao.updateTime("fajr",day.fajr!!)
                        databaseDao.updateTime("dhuhr",day.dhuhr!!)
                        databaseDao.updateTime("asr",day.asr!!)
                        databaseDao.updateTime("maghrib",day.maghrib!!)
                        databaseDao.updateTime("isha",day.isha!!)
                    }
                }
                catch (e:Exception){

                }
                //it : JSON Object
            }, Response.ErrorListener {
                it.printStackTrace()
            })

        // Ajouter la requete http construit dans la file des requetes http
        // pour le lancer
        mRequestQueue.add(mJsonObjectRequest)
    }

}
