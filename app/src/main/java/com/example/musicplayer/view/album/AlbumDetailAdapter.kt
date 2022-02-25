package com.example.musicplayer.view.album

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.databinding.AlbumDetailsItemBinding
import com.example.musicplayer.view.all.SongAdapter

class AlbumDetailAdapter(
    val list: List<Song>,
    val songEventListener: SongAdapter.SongEventListener
) : RecyclerView.Adapter<AlbumDetailAdapter.Holder>() {
    class Holder(val view: AlbumDetailsItemBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(song: Song) {
            view.artist.text = song.artist
            view.songTitle.text = song.songTitle
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding: AlbumDetailsItemBinding =
            AlbumDetailsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener {
            songEventListener.onSelect(list[position], position)
        }
    }

    override fun getItemCount(): Int = list.size
}