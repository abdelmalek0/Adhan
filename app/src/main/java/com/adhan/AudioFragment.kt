package com.adhan

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences.Editor
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.adhan.model.DataSource
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.audio_layout.view.*
import kotlinx.android.synthetic.main.fragment_audio.*


class AudioFragment : Fragment() ,CellClickListener{
    private val MY_PREFS_NAME = "audio"
    var listView = ArrayList<View>()
    var working = false
    var listPlayer : ArrayList<MediaPlayer> = ArrayList<MediaPlayer>()
    lateinit var audioAdapter : AudioRecycleViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audio, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycleView()
        addDataSet()
    }

    fun initRecycleView(){
        recycleView.apply {
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL, false)
            audioAdapter = AudioRecycleViewAdapter(this@AudioFragment)
            adapter = audioAdapter
        }
    }

    fun addDataSet(){
        val data = DataSource.createdataSet()
        audioAdapter.submitList(data)
    }

    override fun onCellClickListener(it: View) {
        if(!working){
            var mp = MediaPlayer.create(context, (it.tag.toString()).toInt())
            stopMediaPlayers()
            listView.add(it)
            listPlayer.add(mp)
            mp.start()
            working = true
            it.audio_img.setImageResource(R.drawable.ic_stop)
        }else{
            stopMediaPlayers()
            working = false
        }
    }

    override fun onButtonClickListener(it: View) {
        val editor: Editor =
            context!!.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit()
        editor.putInt("audio", it.tag.toString().toInt())
        editor.apply()
        (activity as HomeInterface).goHomeButtonChecked()
    }

    private fun stopMediaPlayers(){
        if (listPlayer.size != 0) {
            for (i in 0..listPlayer.size) {
                try {
                    listPlayer[i].stop()
                    listView[i].audio_img.setImageResource(R.drawable.ic_start)

                }catch (e:Exception){}
            }
        }
    }

    override fun onStop() {
        super.onStop()
        stopMediaPlayers()
    }

}