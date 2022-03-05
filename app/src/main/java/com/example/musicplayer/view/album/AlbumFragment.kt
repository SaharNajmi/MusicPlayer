package com.example.musicplayer.view.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.data.db.dao.entities.Album
import com.example.musicplayer.databinding.FragmentAlbumBinding
import com.example.musicplayer.view.main.MainFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumFragment : Fragment(), AlbumAdapter.AlbumEventListener {
    lateinit var binding: FragmentAlbumBinding
    private val viewModel: AlbumViewModel by viewModels()

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