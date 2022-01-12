package com.example.musicplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.musicplayer.databinding.FragmentLyricsSearchBinding

class SearchLyricsFragment : Fragment() {
    lateinit var binding: FragmentLyricsSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLyricsSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //go back
        binding.btnBackPage.setOnClickListener {
            it.findNavController().navigate(R.id.action_searchLyricsFragment_to_detailFragment)
        }
    }
}