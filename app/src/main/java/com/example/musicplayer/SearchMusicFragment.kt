package com.example.musicplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.musicplayer.databinding.FragmentSearchMusicBinding

class SearchMusicFragment : Fragment() {
    lateinit var binding: FragmentSearchMusicBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //go back
        binding.btnBackPage.setOnClickListener {
            it.findNavController().navigate(R.id.action_searchMusicFragment_to_homeFragment)
        }
    }
}