package com.example.musicplayer.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.model.AlbumModel
import com.example.musicplayer.databinding.FragmentAlbumBinding
import com.example.musicplayer.main.MainFragmentDirections
import com.example.musicplayer.main.ViewModelFactory
import com.example.myInterface.AlbumEventListener

class AlbumFragment : Fragment(), AlbumEventListener {
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
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory()
        ).get(AlbumViewModel::class.java)

        //show list album
        showAlbums()
    }

    fun showAlbums() {
        binding.recyclerAlbum.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        binding.recyclerAlbum.adapter =
            AlbumAdapter(requireContext(), viewModel.getAlbums(requireContext()), this)
    }

    override fun onSelect(albumModel: AlbumModel) {
        val directions = MainFragmentDirections.actionMainFragmentToAlbumDetailsFragment(albumModel)
        findNavController().navigate(directions)
    }

}