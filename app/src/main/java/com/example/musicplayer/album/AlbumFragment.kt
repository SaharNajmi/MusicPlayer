package com.example.musicplayer.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adapter.AlbumAdapter
import com.example.model.AlbumModel
import com.example.musicplayer.ReadExternalDate
import com.example.musicplayer.databinding.FragmentAlbumBinding

class AlbumFragment : Fragment() {
    lateinit var binding: FragmentAlbumBinding
    var listAlbum = ArrayList<AlbumModel>()

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
        //get array list album
        val listMusic = ReadExternalDate().readExternalData(requireContext())
        val listAlbumId = ReadExternalDate().getListAlbumId(listMusic)
        listAlbum = ReadExternalDate().getListAlbum(listMusic, listAlbumId)

        //show list album
        showListAlbum()
    }

    fun showListAlbum() {
        binding.recyclerAlbum.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        binding.recyclerAlbum.adapter = AlbumAdapter(requireContext(), listAlbum)
    }

}