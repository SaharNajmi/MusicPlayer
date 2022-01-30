package com.example.musicplayer.file

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.`interface`.FileEventListener
import com.example.musicplayer.databinding.FragmentFileBinding
import java.io.File

class FileFragment : Fragment(), FileEventListener {
    lateinit var binding: FragmentFileBinding
    lateinit var files: List<File>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val file = context?.getExternalFilesDir(null)
        val currentFolder = File(file?.path)
        files = currentFolder.listFiles().toList()

        //show list file
        showFiles()
    }

    private fun showFiles() {
        binding.recyclerFile.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerFile.adapter = FileAdapter(files, this)
    }

    override fun onFileItemClick(file: File) {
        TODO("Not yet implemented")
    }
}