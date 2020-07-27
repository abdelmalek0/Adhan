package com.adhan

import android.app.AlarmManager
import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.app.PendingIntent
import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.adhan.cache.AppDatabase
import com.adhan.cache.Prayer
import com.adhan.cache.PrayerDao
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NotificationService : JobService() {

    private val notificationId = 1001
    private val CHANNEL_ID = "Adhan"
    var listPrayers : ArrayList<Prayer> = ArrayList<Prayer>()

    override fun onCreate() {
        super.onCreate()
        Log.d("TIMING","Create")
    }




    override fun onStartJob(p0: JobParameters?): Boolean {
        var liste = p0?.extras?.getStringArray("salawat")
        var gson = Gson()
        if (liste != null) {
            for(i in liste){
                listPrayers.add(gson.fromJson(i,Prayer::class.java))
            }
        }
        Log.d("TIMING","startCommand")
        val someHandler = Handler(mainLooper)
        var hour : String
        someHandler.postDelayed(object : Runnable {
            override fun run() {
                Log.d("TIMING","run")
                hour = SimpleDateFormat("HH:mm", Locale.US).format(Date())
                for(element in listPrayers){
                    Log.d("TIMING",hour)
                    Log.d("TIMING",element.time)
                    if(hour == element.time)
                    {
                        // Create an explicit intent for an Activity in your app
                        val intent = Intent(baseContext, MainActivity::class.java)
                        val pendingIntent: PendingIntent = PendingIntent.getActivity(baseContext, 0, intent, 0)
                        val builder = NotificationCompat.Builder(baseContext, CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_app)
                            .setContentTitle(baseContext.resources.getString(R.string.title_notification)+element.name)
                            .setContentText(baseContext.resources.getString(R.string.text_notification))
                            .setSound(Uri.parse("android.resource://" + packageName + "/" + R.raw.azan1))
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)

                        with(NotificationManagerCompat.from(baseContext)) {
                            // notificationId is a unique int for each notification that you must define
                            notify(notificationId, builder.build())
                        }
                        break
                    }
                }

                someHandler.postDelayed(this, 60000)
            }
        }, 10)
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return false
    }

}