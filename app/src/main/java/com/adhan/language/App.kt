package com.adhan.language

import android.app.Application
import android.preference.PreferenceManager
import android.util.Log
import com.adhan.MainActivity
import java.util.*

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        var change = ""
        val sharedPreferences = applicationContext.getSharedPreferences("Adhan", 0)
        val language = sharedPreferences.getString("language", "Arabic")
        change = if (language=="English" ) {
            "en"
        }else {
            "ar"
        }

        Log.d("Locallle",change)
        BaseActivity.dLocale = Locale(change) //set any locale you want here
    }
}