package com.example.musicplayer.all

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.`interface`.SongEventListener
import com.example.model.SongModel
import com.example.musicplayer.databinding.FragmentHomeBinding
import com.example.musicplayer.player.Player

class HomeFragment : Fragment(), SongEventListener {
    lateinit var binding: FragmentHomeBinding
    var listMusic = ArrayList<SongModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //get all songs from phone
        listMusic = Player.getListSong(requireContext())

        //show list items into recyclerView
        showListMusic()
    }

    fun showListMusic() {
        binding.recyclerMusics.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMusics.adapter = SongAdapter(requireContext(), listMusic, this)
    }

    override fun onSelect(songModel: SongModel, posSong: Int) {
        Player.setSong(songModel, posSong)
    }
}