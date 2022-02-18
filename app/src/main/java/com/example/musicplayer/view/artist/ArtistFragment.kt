package com.example.musicplayer.view.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.data.db.MusicDatabase
import com.example.musicplayer.data.db.dao.entities.Artist
import com.example.musicplayer.data.repository.LocalMusic
import com.example.musicplayer.data.repository.MusicRepository
import com.example.musicplayer.databinding.FragmentArtistBinding
import com.example.musicplayer.factory.MainViewModelFactory
import com.example.musicplayer.view.main.MainFragmentDirections

class ArtistFragment : Fragment(), ArtistAdapter.ArtistEventListener {
    lateinit var binding: FragmentArtistBinding
    lateinit var viewModel: ArtistViewModel
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

        //viewModel
        val musicDao = MusicDatabase.getInstance(requireContext()).musicDao()
        viewModel = ViewModelProvider(
            requireActivity(),
            MainViewModelFactory(
                MusicRepository(LocalMusic(requireContext()), musicDao)
            )
        ).get(ArtistViewModel::class.java)

        //show list artist
        showArtists()
    }

    private fun showArtists() {
        binding.recyclerAtrist.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerAtrist.adapter =
            ArtistAdapter(requireContext(), viewModel.getArtists() as ArrayList<Artist>, this)
    }

    override fun onSelect(artist: Artist) {
        val directions =
            MainFragmentDirections.actionMainFragmentToArtistDetailFragment(artist)
        findNavController().navigate(directions)
    }
}