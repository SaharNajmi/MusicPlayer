package com.example.musicplayer.file

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.`interface`.SongEventListener
import com.example.model.SongModel
import com.example.musicplayer.all.SongAdapter
import com.example.musicplayer.databinding.FragmentFileDetailBinding
import com.example.musicplayer.player.Player

class FileDetailFragment : Fragment(), SongEventListener {
    lateinit var binding: FragmentFileDetailBinding
    val args: FileDetailFragmentArgs by navArgs()
    var musics = ArrayList<SongModel>()
    var musicsInsideFolder = ArrayList<SongModel>()
    lateinit var myPlayer: Player
    lateinit var folderName: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFileDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        folderName = args.fileDetail
        myPlayer = Player.getInstance()

        //get all musics
        musics = myPlayer.getSongs(requireContext())

        //set Text textView FolderName
        binding.folderName.text = folderName

        //Get list folder items by folderName
        getFolders()

        //show items
        initRecycler()
    }

    fun initRecycler() {
        binding.recyclerDetailFolder.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerDetailFolder.adapter =
            SongAdapter(requireContext(), musicsInsideFolder, this)
    }

    fun getFolders() {
        musics.forEach {
            if (folderName == it.folderName)
                musicsInsideFolder.add(it)
        }
        myPlayer.musics = musicsInsideFolder
    }

    override fun onSelect(songModel: SongModel, posSong: Int) {
        myPlayer.songSelected(songModel, posSong)
    }
}