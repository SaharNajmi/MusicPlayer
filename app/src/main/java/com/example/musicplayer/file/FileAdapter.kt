package com.example.musicplayer.file

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.databinding.FolderItemBinding
import com.example.myInterface.FileEventListener

class FileAdapter(val file: List<String>, val fileItemEventListener: FileEventListener) :
    RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    class FileViewHolder(var view: FolderItemBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(list: String) {
            view.folderName.text = list
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val binding = FolderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(file[position])

        holder.itemView.setOnClickListener {
            fileItemEventListener.onFileItemClick(file[position])
        }
    }

    override fun getItemCount(): Int = file.size
}