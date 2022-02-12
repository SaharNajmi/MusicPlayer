package com.example.musicplayer.all

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.model.SongModel
import com.example.musicplayer.databinding.FragmentHomeBinding
import com.example.musicplayer.main.ViewModelFactory
import com.example.musicplayer.player.Player
import com.example.myInterface.SongEventListener

class HomeFragment : Fragment(), SongEventListener {
    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //viewModel
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory()
        ).get(HomeViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        //show list musics
        showMusics()
    }

    fun showMusics() {
        val musics = viewModel.getMusics(requireContext())
        binding.recyclerMusics.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMusics.adapter = SongAdapter(requireContext(), musics, this)
    }

    override fun onSelect(songModel: SongModel, posSong: Int) {
        Player.getInstance().songSelected(songModel, posSong)
    }
}