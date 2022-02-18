package com.example.musicplayer.view.all

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.data.db.MusicDatabase
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.data.repository.LocalMusic
import com.example.musicplayer.data.repository.MusicRepository
import com.example.musicplayer.databinding.FragmentHomeBinding
import com.example.musicplayer.factory.BaseViewModelFactory
import com.example.musicplayer.player.Player

class HomeFragment : Fragment(), SongAdapter.SongEventListener {
    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: HomeViewModel

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
        //viewModel
        val musicDao = MusicDatabase.getInstance(requireContext()).musicDao()
        viewModel = ViewModelProvider(
            requireActivity(),
            BaseViewModelFactory(
                Player.getInstance(),
                MusicRepository(LocalMusic(requireContext()), musicDao)
            )
        ).get(HomeViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        //show list musics
        showMusics()

        //update list musics
        viewModel.updateList()
    }

    fun showMusics() {
        val musics = viewModel.getMusics()
        binding.recyclerMusics.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMusics.adapter = SongAdapter(
            requireContext(),
            musics as ArrayList<Song>, this
        )
    }

    override fun onSelect(song: Song, posSong: Int) {
        viewModel.songSelected(song, posSong)
    }
}