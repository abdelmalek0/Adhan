package com.adhan

import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adhan.model.Audio
import kotlinx.android.synthetic.main.audio_layout.view.*


class AudioRecycleViewAdapter(private val cellClickListener: CellClickListener)  : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items :List<Audio> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AudioViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.audio_layout,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is AudioViewHolder -> {
                holder.bind(items[position])
                holder.itemView.audio_img.setOnClickListener {
                    cellClickListener.onCellClickListener(holder.itemView)
                }
                holder.itemView.chooseButton.setOnClickListener{
                    cellClickListener.onButtonClickListener(holder.itemView)
                }
            }
        }
    }
    fun submitList(audioList:List<Audio>){
        items = audioList
    }

    class AudioViewHolder constructor(itemView : View): RecyclerView.ViewHolder(itemView){
        fun bind(audio: Audio){
            itemView.tag = audio.src.toString()
            Log.d("clicked", audio.src.toString()+" j")
        }

    }
}