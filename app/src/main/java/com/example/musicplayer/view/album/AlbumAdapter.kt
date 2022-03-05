package com.example.musicplayer.view.album

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.data.db.dao.entities.Album
import com.example.musicplayer.databinding.AlbumItemBinding

class AlbumAdapter(
    val context: Context,
    val list: List<Album>,
    val albumEventListener: AlbumEventListener
) :
    RecyclerView.Adapter<AlbumAdapter.Holder>() {
    inner class Holder(val view: AlbumItemBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(album: Album) {
            view.artist.text = album.artist
            view.albumTitle.text = album.albumName
            Glide.with(context)
                .load(album.albumImage)
                .into(view.coverImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding: AlbumItemBinding =
            AlbumItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list[position])
        //click item
        holder.itemView.setOnClickListener {
            albumEventListener.onSelect(list[position])
        }
    }

    override fun getItemCount(): Int = list.size

    interface AlbumEventListener {
        fun onSelect(album: Album)
    }
}