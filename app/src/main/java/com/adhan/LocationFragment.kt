package com.adhan

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.android.synthetic.main.fragment_location.*
import kotlinx.coroutines.*
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader


class LocationFragment : Fragment() {

    var listCities: ArrayList<String> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runBlocking {
            getListCities()
        }
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            context!!,
            android.R.layout.simple_dropdown_item_1line, listCities
        )
        val textView: AutoCompleteTextView =
                activity?.findViewById(R.id.textInputEditText) as AutoCompleteTextView
            textView.setAdapter(adapter)

        confirmButton.setOnClickListener{
            sendLocation(textView.text.toString())
            try {
                val imm: InputMethodManager? =
                    activity!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
                imm!!.hideSoftInputFromWindow(activity!!.currentFocus!!.windowToken, 0)
            }catch (e: Exception){}

            (activity as MainActivity).goToHomeFragment()
            (activity as HomeInterface).goHomeButtonChecked()
        }

        gpsLocator.setOnClickListener{
            val i = Intent(context, GPSTracker::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context!!.startService(i)

            (activity as MainActivity).goToHomeFragment()
            (activity as HomeInterface).goHomeButtonChecked()
        }

    }


    private fun getListCities() {
        val bufferedInputStream =
            BufferedInputStream(resources.openRawResource(R.raw.world_cities_names))
        val bufferedReader = BufferedReader(
            InputStreamReader(bufferedInputStream)
        )
        try {
            var line: String = ""
            while (bufferedReader.readLine().also { line = it } != null) {
                if(line.isNotEmpty()) listCities.add(line)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sendLocation(location: String){
        val locationSplitted =location.split(",")
        val country = locationSplitted[1].split(" ").joinToString("-")
        val city = locationSplitted[0].split(" ").joinToString("-")
        val intent = Intent("ManualLocationUpdates")
        intent.putExtra("country", country)
        Log.d("tagging", country)
        intent.putExtra("city", city)
        Log.d("tagging", city)
        LocalBroadcastManager.getInstance(context!!).sendBroadcast(intent)
        Log.d("tagging", "sendLocation")
    }


}