package com.example.musicplayer.view.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.databinding.FragmentFavoriteBinding
import com.example.musicplayer.player.Player
import com.example.musicplayer.view.all.SongAdapter

class FavoriteFragment : Fragment(), SongAdapter.SongEventListener {
    lateinit var binding: FragmentFavoriteBinding
    lateinit var favoriteViewModel: FavoriteViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //favorite ViewModel
        favoriteViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(activity?.application!!)
            ).get(FavoriteViewModel::class.java)

        Log.d("AAAAA", favoriteViewModel.getAll().toString())


        //show list favorite
        initRecycler()

        //go back
        binding.btnBackPage.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    fun initRecycler() {
        binding.recyclerFavorite.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerFavorite.adapter = SongAdapter(
            requireContext(),
            favoriteViewModel.getAll(), this
        )
    }

    override fun onSelect(songModel: SongModel, posSong: Int) {
        Player.getInstance(requireContext()).songSelected(songModel, posSong)
    }
}