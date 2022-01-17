package com.example.musicplayer.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adapter.AlbumAdapter
import com.example.musicplayer.databinding.FragmentAlbumBinding
import com.model.AlbumModel

class AlbumFragment : Fragment() {
    lateinit var binding: FragmentAlbumBinding
    val listAlbum = ArrayList<AlbumModel>()

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
        //showListAlbum()
    }

    fun showListAlbum() {
        binding.recyclerAlbum.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerAlbum.adapter = AlbumAdapter(requireContext(), listAlbum)
    }

}