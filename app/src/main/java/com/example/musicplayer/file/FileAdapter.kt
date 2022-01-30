package com.example.musicplayer.file

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.`interface`.FileEventListener
import com.example.musicplayer.databinding.FolderItemBinding
import java.io.File

class FileAdapter(val files: List<File>, val fileItemEventListener: FileEventListener) :
    RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    class FileViewHolder(var view: FolderItemBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(file: File) {
            view.folderName.text = file.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val binding = FolderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(files[position])

        holder.itemView.setOnClickListener {
            fileItemEventListener.onFileItemClick(files[position])
        }
    }

    override fun getItemCount(): Int = files.size
}