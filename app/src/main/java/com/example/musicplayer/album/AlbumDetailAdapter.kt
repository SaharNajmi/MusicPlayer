package com.example.musicplayer.album

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.model.SongModel
import com.example.musicplayer.databinding.AlbumDetailsItemBinding
import com.example.myInterface.SongEventListener

class AlbumDetailAdapter(
    val list: ArrayList<SongModel>,
    val songEventListener: SongEventListener
) : RecyclerView.Adapter<AlbumDetailAdapter.Holder>() {
    class Holder(val view: AlbumDetailsItemBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(songModel: SongModel) {
            view.artist.text = songModel.artist
            view.songTitle.text = songModel.songTitle
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