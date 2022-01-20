package com.example.adapter

import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.model.AlbumModel
import com.example.musicplayer.databinding.AlbumItemBinding

class AlbumAdapter(val context: Context, val list: ArrayList<AlbumModel>) :
    RecyclerView.Adapter<AlbumAdapter.Holder>() {
    inner class Holder(val view: AlbumItemBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(albumModel: AlbumModel) {
            view.artist.text = albumModel.artist
            view.albumTitle.text = albumModel.albumName
            var bitmap: Bitmap? = null
            try {
                bitmap =
                    MediaStore.Images.Media.getBitmap(
                        context.contentResolver,
                        albumModel.albumImage
                    )
                view.coverImage.setImageBitmap(bitmap)
            } catch (e: Exception) {
                Log.e("Error set image!!", e.toString())
            }
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
            //
        }
    }

    override fun getItemCount(): Int = list.size
}