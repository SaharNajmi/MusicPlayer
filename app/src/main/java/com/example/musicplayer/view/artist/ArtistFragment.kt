package com.example.musicplayer.view.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.data.db.dao.entities.Artist
import com.example.musicplayer.databinding.FragmentArtistBinding
import com.example.musicplayer.view.main.MainFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtistFragment : Fragment(), ArtistAdapter.ArtistEventListener {
    lateinit var binding: FragmentArtistBinding
    private val viewModel: ArtistViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArtistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showArtists()
    }

    private fun showArtists() {
        binding.recyclerAtrist.layoutManager = LinearLayoutManager(requireContext())
        viewModel.artists.observe(viewLifecycleOwner, { list ->
            binding.recyclerAtrist.adapter =
                ArtistAdapter(requireContext(), list, this)
        })
    }

    override fun onSelect(artist: Artist) {
        val directions =
            MainFragmentDirections.actionMainFragmentToArtistDetailFragment(artist)
        findNavController().navigate(directions)
    }
}