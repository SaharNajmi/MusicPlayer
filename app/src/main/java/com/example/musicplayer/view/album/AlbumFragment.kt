package com.example.musicplayer.view.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.data.db.MusicDatabase
import com.example.musicplayer.data.db.dao.entities.Album
import com.example.musicplayer.data.repository.LocalMusic
import com.example.musicplayer.data.repository.MusicRepository
import com.example.musicplayer.databinding.FragmentAlbumBinding
import com.example.musicplayer.factory.MainViewModelFactory
import com.example.musicplayer.view.main.MainFragmentDirections

class AlbumFragment : Fragment(), AlbumAdapter.AlbumEventListener {
    lateinit var binding: FragmentAlbumBinding
    lateinit var viewModel: AlbumViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAlbumBinding.inflate(inflater, container, false)
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
        ).get(AlbumViewModel::class.java)

        //show list album
        showAlbums()
    }

    private fun showAlbums() {
        binding.recyclerAlbum.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        viewModel.albums.observe(viewLifecycleOwner, { list ->
            binding.recyclerAlbum.adapter =
                AlbumAdapter(requireContext(), list, this)
        })
    }

    override fun onSelect(album: Album) {
        val directions = MainFragmentDirections.actionMainFragmentToAlbumDetailsFragment(album)
        findNavController().navigate(directions)
    }

}