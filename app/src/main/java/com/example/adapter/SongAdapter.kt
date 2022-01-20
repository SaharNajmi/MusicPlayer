package com.example.adapter

import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.`interface`.OnSongPosition
import com.example.`interface`.SongEventListener
import com.example.model.SongModel
import com.example.musicplayer.databinding.MusicItemLayoutBinding


class SongAdapter(
    val context: Context,
    val list: ArrayList<SongModel>,
    val eventListener: SongEventListener,
    val songPosition: OnSongPosition
) :
    RecyclerView.Adapter<SongAdapter.Holder>() {

    inner class Holder(val view: MusicItemLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(songModel: SongModel) {
            view.artist.text = songModel.artist
            view.songTitle.text = songModel.songTitle
            var bitmap: Bitmap? = null
            try {
                bitmap =
                    MediaStore.Images.Media.getBitmap(context.contentResolver, songModel.coverImage)
                view.coverImage.setImageBitmap(bitmap)
            } catch (e: Exception) {
                Log.e("Error set image!!", e.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding: MusicItemLayoutBinding =
            MusicItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val myList = list[position]
        holder.bind(myList)

        holder.itemView.setOnClickListener {
            eventListener.onSelect(list[position])
            songPosition.onSongPosition(position)
        }
    }

    override fun getItemCount(): Int = list.size
}

