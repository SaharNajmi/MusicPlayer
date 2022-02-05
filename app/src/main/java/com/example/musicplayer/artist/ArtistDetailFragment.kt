package com.example.musicplayer.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.`interface`.SongEventListener
import com.example.model.ArtistModel
import com.example.model.SongModel
import com.example.musicplayer.all.SongAdapter
import com.example.musicplayer.databinding.FragmentArtistDetailBinding
import com.example.musicplayer.main.ViewModelFactory
import com.example.musicplayer.player.Player

class ArtistDetailFragment : Fragment(), SongEventListener {
    lateinit var binding: FragmentArtistDetailBinding
    lateinit var artistModel: ArtistModel
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
        artistModel = args.artistDetail

        //viewModel
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory()
        ).get(ArtistDetailViewModel::class.java)

        //set title
        binding.artist.text = artistModel.artist

        //show items
        initRecycler()
    }

    fun initRecycler() {
        //Get list artist items by artistId
        val artists = viewModel.getArtists(artistModel.id, requireContext())
        binding.recyclerDetailArtist.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerDetailArtist.adapter = SongAdapter(requireContext(), artists, this)
    }

    override fun onSelect(songModel: SongModel, posSong: Int) {
        Player.getInstance().songSelected(songModel, posSong)
    }
}