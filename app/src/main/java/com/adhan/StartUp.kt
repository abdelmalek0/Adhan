package com.adhan

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class StartUp : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val  shared = context.getSharedPreferences("notification", Context.MODE_PRIVATE)
            val bool = shared.getBoolean("notification",true)
            if(bool){
                val i = Intent(context, DataGetter::class.java)
                context.startService(i)
            }
        }
    }

}