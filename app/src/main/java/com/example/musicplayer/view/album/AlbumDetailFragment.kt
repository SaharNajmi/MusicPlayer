package com.example.musicplayer.view.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.musicplayer.data.db.MusicDatabase
import com.example.musicplayer.data.db.dao.entities.Album
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.data.repository.LocalMusic
import com.example.musicplayer.data.repository.MusicRepository
import com.example.musicplayer.databinding.FragmentAlbumDetailBinding
import com.example.musicplayer.factory.BaseViewModelFactory
import com.example.musicplayer.player.Player
import com.example.musicplayer.view.all.SongAdapter

class AlbumDetailFragment : Fragment(), SongAdapter.SongEventListener {
    lateinit var binding: FragmentAlbumDetailBinding
    lateinit var album: Album
    lateinit var viewModel: AlbumDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: AlbumDetailFragmentArgs by navArgs()
        album = args.albumDetail

        //viewModel
        val musicDao = MusicDatabase.getInstance(requireContext()).musicDao()
        viewModel = ViewModelProvider(
            requireActivity(),
            BaseViewModelFactory(
                Player.getInstance(),
                MusicRepository(LocalMusic(requireContext()), musicDao)
            )
        ).get(AlbumDetailViewModel::class.java)

        //set data
        updateUI()

        //show items
        initRecycler()
    }

    fun updateUI() {
        binding.albumTitle.text = album.albumName
        binding.artist.text = album.artist
        Glide.with(this)
            .load(album.albumImage)
            .into(binding.imgCoverAlbum)
    }

    fun initRecycler() {
        binding.recyclerDetailAlbum.layoutManager = LinearLayoutManager(requireContext())
        //Get musics by albumId
        viewModel.getMusics(album.id).observe(viewLifecycleOwner, { list ->
            binding.recyclerDetailAlbum.adapter = AlbumDetailAdapter(list, this)
        })
    }

    override fun onSelect(song: Song, posSong: Int) {
        viewModel.songSelected(song, posSong)
    }
}