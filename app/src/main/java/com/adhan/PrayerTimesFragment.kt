package com.adhan

import android.app.job.JobScheduler
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.util.LayoutDirection
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.adhan.cache.AppDatabase
import com.adhan.cache.PrayerDao
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_prayer_times.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.lang.Exception


class PrayerTimesFragment : Fragment() {
    lateinit var databaseDao : PrayerDao
    var listPrayerTimes = ArrayList<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_prayer_times, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val lang = (activity as MainActivity).sharedPreferences.getString("language","Arabic")
        if(lang == "Arabic"){
            prayerBox.layoutDirection = View.LAYOUT_DIRECTION_LTR
        }else{
            prayerBox.layoutDirection = View.LAYOUT_DIRECTION_RTL
        }
        super.onViewCreated(view, savedInstanceState)

        val  shared = context!!.getSharedPreferences("notification", Context.MODE_PRIVATE)
        val bool = shared.getBoolean("notification",true)
        switch1.isChecked = bool

        switch1.setOnCheckedChangeListener { _, isChecked ->
            val editor: Editor = activity?.getSharedPreferences("notification", MODE_PRIVATE)!!.edit()
            if (isChecked) {
                editor.putBoolean("notification",true)
                val i = Intent(context, DataGetter::class.java)
                context!!.startService(i)
            } else {
                editor.putBoolean("notification",false)
                val scheduler = context!!.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
                scheduler.cancelAll()
                Log.d("scd","Job cancelled")
            }
            editor.apply()
        }
        Log.d("len","${listPrayerTimes.size}")
        databaseDao = AppDatabase.getInstance(context!!).prayerDao()
        runBlocking {
            loadData()
            salat1.text = listPrayerTimes[0]
            salat2.text = listPrayerTimes[1]
            salat3.text = listPrayerTimes[2]
            salat4.text = listPrayerTimes[3]
            salat5.text = listPrayerTimes[4]
        }

    }
    private suspend fun loadData(){
        try {
        listPrayerTimes.add(databaseDao.findByName("صلاة الفجْر"))
        listPrayerTimes.add(databaseDao.findByName("صلاة الظُّهْر"))
        listPrayerTimes.add(databaseDao.findByName("صلاة العَصر"))
        listPrayerTimes.add(databaseDao.findByName("صلاة المَغرب"))
        listPrayerTimes.add(databaseDao.findByName("صلاة العِشاء"))
        }catch (e:Exception){}
    }


}