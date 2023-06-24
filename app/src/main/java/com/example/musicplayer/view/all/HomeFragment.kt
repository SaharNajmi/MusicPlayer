package com.example.musicplayer.view.all

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), SongAdapter.SongEventListener {
    lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        showMusics()

    }

    private fun showMusics() {
        binding.recyclerMusics.layoutManager = LinearLayoutManager(requireContext())
        viewModel.musics.observe(viewLifecycleOwner, { list ->
            binding.recyclerMusics.adapter = SongAdapter(
                requireContext(),
                list, this
            )
        })

    }

    override fun onSelect(song: Song, posSong: Int) {
        viewModel.songSelected(song, posSong)
    }
}