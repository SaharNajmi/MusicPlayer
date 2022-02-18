package com.example.musicplayer.view.all

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.databinding.MusicItemLayoutBinding

class SongAdapter(
    val context: Context,
    var list: ArrayList<Song>,
    val eventListener: SongEventListener
) :
    RecyclerView.Adapter<SongAdapter.Holder>() {

    inner class Holder(val view: MusicItemLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(song: Song) {
            view.artist.text = song.artist
            view.songTitle.text = song.songTitle
            Glide.with(context)
                .load(song.coverImage)
                .into(view.coverImage)
        }
    }

    /*fun loadImage(imageView: ImageView, uri: Uri) {
        var bitmap: Bitmap? = null
        try {
            bitmap =
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            imageView.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Log.e("Error set image!!", e.toString())
        }
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding: MusicItemLayoutBinding =
            MusicItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val myList = list[position]
        holder.bind(myList)

        holder.itemView.setOnClickListener {
            eventListener.onSelect(list[position], position)
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateList(songs: ArrayList<Song>) {
        list = songs
        notifyDataSetChanged()
    }

    interface SongEventListener {
        fun onSelect(song: Song, posSong: Int)
    }
}

