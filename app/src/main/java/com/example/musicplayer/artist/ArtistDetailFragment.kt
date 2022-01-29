package com.example.musicplayer.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.`interface`.SongEventListener
import com.example.model.ArtistModel
import com.example.model.SongModel
import com.example.musicplayer.all.SongAdapter
import com.example.musicplayer.databinding.FragmentArtistDetailBinding
import com.example.musicplayer.player.Player

class ArtistDetailFragment : Fragment(), SongEventListener {

    lateinit var binding: FragmentArtistDetailBinding
    lateinit var artistModel: ArtistModel
    val args: ArtistDetailFragmentArgs by navArgs()
    var artists = ArrayList<SongModel>()
    var musics = ArrayList<SongModel>()
    lateinit var myPlayer: Player

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
        artistModel = args.artistDetail
        myPlayer = Player.getInstance()

        musics = myPlayer.getSongs(requireContext())

        binding.artist.text = artistModel.artist

        //Get list artist items by artistId
        getArtists()

        //show items
        initRecycler()
    }

    fun initRecycler() {
        binding.recyclerDetailArtist.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerDetailArtist.adapter = SongAdapter(requireContext(), artists, this)
    }

    fun getArtists() {
        val artistId = artistModel.id
        musics.forEach {
            if (artistId == it.artistID)
                artists.add(it)
        }

        myPlayer.musics = artists
    }

    override fun onSelect(songModel: SongModel, posSong: Int) {
        myPlayer.songSelected(songModel, posSong)
    }
}