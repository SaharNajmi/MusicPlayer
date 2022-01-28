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
import com.example.musicplayer.ReadExternalDate
import com.example.musicplayer.databinding.FragmentArtistBinding
import com.example.musicplayer.main.MainFragmentDirections
import com.example.musicplayer.player.Player

class ArtistFragment : Fragment(), ArtistEventListener {
    lateinit var binding: FragmentArtistBinding
    var listArtist = ArrayList<ArtistModel>()
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

        //get array list artist
        val listMusic = Player.getListSong(requireContext())
        val listArtistId = ReadExternalDate().getListArtistId(listMusic)
        listArtist = ReadExternalDate().getListArtist(listMusic, listArtistId)

        //show list artist
        showListArtist()
    }

    private fun showListArtist() {
        binding.recyclerAtrist.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerAtrist.adapter = ArtistAdapter(requireContext(), listArtist, this)

    }

    override fun onSelect(artistModel: ArtistModel) {
        val directions =
            MainFragmentDirections.actionMainFragmentToArtistDetailFragment(artistModel)
        findNavController().navigate(directions)
    }
}