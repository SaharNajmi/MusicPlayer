package com.example.musicplayer.view.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.data.db.dao.entities.Artist
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.databinding.FragmentArtistDetailBinding
import com.example.musicplayer.view.all.SongAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtistDetailFragment : Fragment(), SongAdapter.SongEventListener {
    lateinit var binding: FragmentArtistDetailBinding
    private val viewModel: ArtistDetailViewModel by viewModels()
    lateinit var artist: Artist

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArtistDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: ArtistDetailFragmentArgs by navArgs()
        artist = args.artistDetail

        binding.artist.text = artist.artist

        initRecycler()
    }

    fun initRecycler() {
        binding.recyclerDetailArtist.layoutManager = LinearLayoutManager(requireContext())
        viewModel.getMusics(artist.id).observe(viewLifecycleOwner, { list ->
            binding.recyclerDetailArtist.adapter = SongAdapter(
                requireContext(),
                list, this
            )
        })
    }

    override fun onSelect(song: Song, posSong: Int) {
        viewModel.songSelected(song, posSong)
    }
}