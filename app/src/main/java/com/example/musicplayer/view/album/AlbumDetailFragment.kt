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
import com.example.musicplayer.data.model.AlbumModel
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.databinding.FragmentAlbumDetailBinding
import com.example.musicplayer.factory.BaseViewModelFactory
import com.example.musicplayer.player.Player
import com.example.musicplayer.view.all.SongAdapter

class AlbumDetailFragment : Fragment(), SongAdapter.SongEventListener {
    lateinit var binding: FragmentAlbumDetailBinding
    lateinit var albumModel: AlbumModel
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
        albumModel = args.albumDetail

        //viewModel
        viewModel = ViewModelProvider(
            requireActivity(),
            BaseViewModelFactory(requireContext())
        ).get(AlbumDetailViewModel::class.java)

        //set data
        updateUI()

        //show items
        initRecycler()
    }

    fun updateUI() {
        binding.albumTitle.text = albumModel.albumName
        binding.artist.text = albumModel.artist
        Glide.with(this)
            .load(albumModel.albumImage)
            .into(binding.imgCoverAlbum)
    }

    fun initRecycler() {
        //Get list album items by albumId
        val albums = viewModel.getAlbums(albumModel.id, requireContext())
        binding.recyclerDetailAlbum.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerDetailAlbum.adapter = AlbumDetailAdapter(albums, this)
    }

    override fun onSelect(songModel: SongModel, posSong: Int) {
        Player.getInstance(requireContext()).songSelected(songModel, posSong)
    }
}