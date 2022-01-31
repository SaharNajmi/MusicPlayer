package com.example.musicplayer.file

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.`interface`.FileEventListener
import com.example.model.SongModel
import com.example.musicplayer.ReadExternalMusic
import com.example.musicplayer.databinding.FragmentFileBinding
import com.example.musicplayer.main.MainFragmentDirections
import com.example.musicplayer.player.Player

class FileFragment : Fragment(), FileEventListener {
    lateinit var binding: FragmentFileBinding
    lateinit var folders: List<String>
    lateinit var musics: ArrayList<SongModel>
    lateinit var myPlayer: Player

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
        myPlayer = Player.getInstance()

        //get list folders
        musics = myPlayer.getSongs(requireContext())
        folders = ReadExternalMusic().getFolderNames(musics)

        //show list folders
        showFolders()
    }

    private fun showFolders() {
        binding.recyclerFile.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerFile.adapter = FileAdapter(folders, this)
    }

    override fun onFileItemClick(fileName: String) {
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToFileDetailFragment(
                fileName
            )
        )
    }
}