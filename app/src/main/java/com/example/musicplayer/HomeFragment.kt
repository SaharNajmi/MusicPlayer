package com.example.musicplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.musicplayer.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
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
        //go to favorite frg
        binding.favorite.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_favoriteFragment)
        }
        //go to search music frg
        binding.search.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_searchMusicFragment)
        }
    }
}