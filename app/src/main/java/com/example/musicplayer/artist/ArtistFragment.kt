package com.example.musicplayer.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.`interface`.ArtistEventListener
import com.example.model.ArtistModel
import com.example.musicplayer.ReadExternalMusic
import com.example.musicplayer.databinding.FragmentArtistBinding
import com.example.musicplayer.main.MainFragmentDirections
import com.example.musicplayer.player.Player

class ArtistFragment : Fragment(), ArtistEventListener {
    lateinit var binding: FragmentArtistBinding
    var artists = ArrayList<ArtistModel>()
    lateinit var myPlayer: Player

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentArtistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myPlayer = Player.getInstance()

        //get array list artist
        val musics = myPlayer.getSongs(requireContext())
        val artistIDs = ReadExternalMusic().getArtistIDs(musics)
        artists = ReadExternalMusic().getArtists(musics, artistIDs)

        //show list artist
        showArtists()
    }

    private fun showArtists() {
        binding.recyclerAtrist.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerAtrist.adapter = ArtistAdapter(requireContext(), artists, this)

    }

    override fun onSelect(artistModel: ArtistModel) {
        val directions =
            MainFragmentDirections.actionMainFragmentToArtistDetailFragment(artistModel)
        findNavController().navigate(directions)
    }
}