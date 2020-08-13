package com.adhan

import android.app.job.JobScheduler
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.adhan.cache.AppDatabase
import com.adhan.cache.PrayerDao
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_prayer_times.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        super.onViewCreated(view, savedInstanceState)

        val  shared = context!!.getSharedPreferences("notification", Context.MODE_PRIVATE)
        val bool = shared.getBoolean("notification",true)
        switch1.isChecked = bool

        databaseDao = AppDatabase.getInstance(context!!).prayerDao()
        CoroutineScope(IO).launch {
            loadData()
            withContext(Main){
                salat1.text = listPrayerTimes[0]
                salat2.text = listPrayerTimes[1]
                salat3.text = listPrayerTimes[2]
                salat4.text = listPrayerTimes[3]
                salat5.text = listPrayerTimes[4]
            }
        }
        /*val sharedPref = activity?.getSharedPreferences("location",Context.MODE_PRIVATE) ?: return
        val locality = sharedPref.getString("baladiya", "___")
        var snackbar = Snackbar.make(prayerTimesFragment, context!!.resources.getString(R.string.place)+locality, Snackbar.LENGTH_INDEFINITE)
        ViewCompat.setLayoutDirection(snackbar.view,ViewCompat.LAYOUT_DIRECTION_RTL)
        snackbar.show()
        */
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
    }
    private suspend fun loadData(){
        try {

        }catch (e:Exception){}
        listPrayerTimes.add(databaseDao.findByName(context!!.resources.getString(R.string.salatSob7)))
        listPrayerTimes.add(databaseDao.findByName(context!!.resources.getString(R.string.salatDohr)))
        listPrayerTimes.add(databaseDao.findByName(context!!.resources.getString(R.string.salat3asr)))
        listPrayerTimes.add(databaseDao.findByName(context!!.resources.getString(R.string.salatMaghreb)))
        listPrayerTimes.add(databaseDao.findByName(context!!.resources.getString(R.string.salatAishaa)))

    }


}