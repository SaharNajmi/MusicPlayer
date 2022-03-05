package com.example.musicplayer.view.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.musicplayer.data.db.dao.entities.Album
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.databinding.FragmentAlbumDetailBinding
import com.example.musicplayer.view.all.SongAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumDetailFragment : Fragment(), SongAdapter.SongEventListener {
    lateinit var binding: FragmentAlbumDetailBinding
    lateinit var album: Album
    private val viewModel: AlbumDetailViewModel by viewModels()

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