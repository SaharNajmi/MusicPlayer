package com.example.musicplayer.view.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.data.model.ArtistModel
import com.example.musicplayer.databinding.FragmentArtistBinding
import com.example.musicplayer.factory.BaseViewModelFactory
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
        viewModel = ViewModelProvider(
            requireActivity(),
            BaseViewModelFactory(requireContext())
        ).get(ArtistViewModel::class.java)

        //show list artist
        showArtists()
    }

    private fun showArtists() {
        binding.recyclerAtrist.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerAtrist.adapter =
            ArtistAdapter(requireContext(), viewModel.getArtists(requireContext()), this)
    }

    override fun onSelect(artistModel: ArtistModel) {
        val directions =
            MainFragmentDirections.actionMainFragmentToArtistDetailFragment(artistModel)
        findNavController().navigate(directions)
    }
}