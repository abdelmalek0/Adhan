package com.adhan

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class StartUp : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val i = Intent(context, DataGetter::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startService(i)
        }
    }

}