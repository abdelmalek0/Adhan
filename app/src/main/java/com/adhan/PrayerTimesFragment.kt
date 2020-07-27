package com.adhan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adhan.R
import com.adhan.cache.AppDatabase
import com.adhan.cache.Prayer
import com.adhan.cache.PrayerDao
import kotlinx.android.synthetic.main.fragment_prayer_times.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    }
    suspend fun loadData(){
        listPrayerTimes.add(databaseDao.findByName(context!!.resources.getString(R.string.salatSob7)))
        listPrayerTimes.add(databaseDao.findByName(context!!.resources.getString(R.string.salatDohr)))
        listPrayerTimes.add(databaseDao.findByName(context!!.resources.getString(R.string.salat3asr)))
        listPrayerTimes.add(databaseDao.findByName(context!!.resources.getString(R.string.salatMaghreb)))
        listPrayerTimes.add(databaseDao.findByName(context!!.resources.getString(R.string.salatAishaa)))

    }


}