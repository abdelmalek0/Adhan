package com.adhan

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlin.system.exitProcess


class SettingsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = activity!!.getSharedPreferences("Adhan", 0)
        val position =  if(sharedPreferences.getString("language","Arabic") == "Arabic" ) {0}else{1}

        ArrayAdapter.createFromResource(
            context!!,
            R.array.languages_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
            spinner.setSelection(position)
        }

        confirmLanguage.setOnClickListener {

            val editor: Editor = sharedPreferences.edit()
            val lang = if(spinner.selectedItemPosition == 0) {"Arabic"}else{"English"}
            editor.putString("language", lang)
            editor.commit()
            restartApp()

        }
        var themeName = when(sharedPreferences.getInt("theme",0)) {
            1 -> R.id.radioTheme1
            2 -> R.id.radioTheme2
            else -> R.id.radioDefault
        }
        chooseTheme.check(themeName)
        chooseTheme.setOnCheckedChangeListener { _, i ->
            val editor: Editor = sharedPreferences.edit()


            when (i) {
                R.id.radioTheme1 -> editor.putInt("theme",1)
                R.id.radioTheme2 -> editor.putInt("theme",2)
                else -> editor.putInt("theme",0)
            }

            editor.commit()
            triggerRestart(activity!!)

        }
    }

    private fun restartApp() {
        val intent = Intent(
            activity!!.applicationContext,
            MainActivity::class.java
        )
        val mPendingIntentId: Int = 15
        val mPendingIntent = PendingIntent.getActivity(
            activity!!.applicationContext,
            mPendingIntentId,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val mgr = activity!!.applicationContext
            .getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mgr[AlarmManager.RTC, System.currentTimeMillis() + 100] = mPendingIntent
        exitProcess(0)
    }
    fun triggerRestart(context: Activity) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        if (context is Activity) {
            (context as Activity).finish()
        }
        Runtime.getRuntime().exit(0)
    }
}