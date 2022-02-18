package com.example.musicplayer.view.artist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.data.db.dao.entities.Artist
import com.example.musicplayer.databinding.ArtistItemBinding

class ArtistAdapter(
    val context: Context,
    val list: ArrayList<Artist>,
    val artistEventListener: ArtistEventListener
) :
    RecyclerView.Adapter<ArtistAdapter.Holder>() {
    inner class Holder(val view: ArtistItemBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(artist: Artist) {
            view.artist.text = artist.artist
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding: ArtistItemBinding =
            ArtistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list[position])
        //click item
        holder.itemView.setOnClickListener {
            artistEventListener.onSelect(list[position])
        }
    }

    override fun getItemCount(): Int = list.size

    interface ArtistEventListener {
        fun onSelect(artist: Artist)
    }
}