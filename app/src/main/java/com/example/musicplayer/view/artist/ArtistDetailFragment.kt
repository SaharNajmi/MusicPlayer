package com.example.musicplayer.view.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.data.db.MusicDatabase
import com.example.musicplayer.data.db.dao.entities.Artist
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.data.repository.LocalMusic
import com.example.musicplayer.data.repository.MusicRepository
import com.example.musicplayer.databinding.FragmentArtistDetailBinding
import com.example.musicplayer.factory.BaseViewModelFactory
import com.example.musicplayer.player.Player
import com.example.musicplayer.view.all.SongAdapter

class ArtistDetailFragment : Fragment(), SongAdapter.SongEventListener {
    lateinit var binding: FragmentArtistDetailBinding
    lateinit var artist: Artist
    lateinit var viewModel: ArtistDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentArtistDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: ArtistDetailFragmentArgs by navArgs()
        artist = args.artistDetail

        //viewModel
        val musicDao = MusicDatabase.getInstance(requireContext()).musicDao()
        viewModel = ViewModelProvider(
            requireActivity(),
            BaseViewModelFactory(
                Player.getInstance(),
                MusicRepository(LocalMusic(requireContext()), musicDao)
            )
        ).get(ArtistDetailViewModel::class.java)

        //update list musics
        viewModel.updateList(artist.id)

        //set title
        binding.artist.text = artist.artist

        //show items
        initRecycler()
    }

    fun initRecycler() {
        //Get list artist items by artistId
        val artists = viewModel.getArtistById(artist.id)
        binding.recyclerDetailArtist.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerDetailArtist.adapter = SongAdapter(
            requireContext(),
            artists as ArrayList<Song>, this
        )
    }

    override fun onSelect(song: Song, posSong: Int) {
        viewModel.songSelected(song, posSong)
    }
}