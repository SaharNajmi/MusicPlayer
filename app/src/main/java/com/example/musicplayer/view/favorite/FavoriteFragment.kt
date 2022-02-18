package com.example.musicplayer.view.favorite

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
import com.example.musicplayer.databinding.FragmentFavoriteBinding
import com.example.musicplayer.factory.BaseViewModelFactory
import com.example.musicplayer.player.Player
import com.example.musicplayer.view.all.SongAdapter

class FavoriteFragment : Fragment(), SongAdapter.SongEventListener {
    lateinit var binding: FragmentFavoriteBinding
    lateinit var favoriteViewModel: FavoriteViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //favorite ViewModel
        val musicDao = MusicDatabase.getInstance(requireContext()).musicDao()
        favoriteViewModel = ViewModelProvider(
            requireActivity(),
            BaseViewModelFactory(
                Player.getInstance(),
                MusicRepository(LocalMusic(requireContext()), musicDao)
            )
        ).get(FavoriteViewModel::class.java)

        //update list musics
        favoriteViewModel.updateList()

        //show list favorite
        initRecycler()

        //go back
        binding.btnBackPage.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    fun initRecycler() {
        binding.recyclerFavorite.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerFavorite.adapter = SongAdapter(
            requireContext(),
            favoriteViewModel.getFavorites() as ArrayList<Song>, this
        )
    }

    override fun onSelect(song: Song, posSong: Int) {
        favoriteViewModel.songSelected(song, posSong)
    }
}