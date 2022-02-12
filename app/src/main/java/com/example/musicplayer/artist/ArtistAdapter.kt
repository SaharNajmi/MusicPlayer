package com.example.musicplayer.artist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.model.ArtistModel
import com.example.musicplayer.databinding.ArtistItemBinding
import com.example.myInterface.ArtistEventListener

class ArtistAdapter(
    val context: Context,
    val list: ArrayList<ArtistModel>,
    val artistEventListener: ArtistEventListener
) :
    RecyclerView.Adapter<ArtistAdapter.Holder>() {
    inner class Holder(val view: ArtistItemBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(artistModel: ArtistModel) {
            view.artist.text = artistModel.artist
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
}