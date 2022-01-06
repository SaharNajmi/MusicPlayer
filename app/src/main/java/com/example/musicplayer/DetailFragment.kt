package com.example.musicplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.musicplayer.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {
    lateinit var binding: FragmentDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //search lyrics
        binding.btnSearchLyrics.setOnClickListener {
            it.findNavController().navigate(R.id.action_detailFragment_to_searchLyricsFragment)
        }

        //go back
        binding.btnDown.setOnClickListener {
            it.findNavController().navigate(R.id.action_detailFragment_to_homeFragment)
        }
    }
}