package com.example.musicplayer.view.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.databinding.FragmentFavoriteBinding
import com.example.musicplayer.view.all.SongAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(), SongAdapter.SongEventListener {
    lateinit var binding: FragmentFavoriteBinding
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()

        binding.btnBackPage.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    fun initRecycler() {
        binding.recyclerFavorite.layoutManager = LinearLayoutManager(requireContext())
        favoriteViewModel.getFavorites().observe(viewLifecycleOwner, { list ->
            binding.recyclerFavorite.adapter = SongAdapter(
                requireContext(),
                list, this
            )
        })
    }

    override fun onSelect(song: Song, posSong: Int) {
        favoriteViewModel.songSelected(song, posSong)
    }
}