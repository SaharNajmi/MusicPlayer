package com.example.musicplayer.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.model.SongModel
import com.example.musicplayer.databinding.FragmentLyricsSearchBinding
import com.example.musicplayer.main.ViewModelFactory

class SearchLyricsFragment : Fragment() {
    lateinit var binding: FragmentLyricsSearchBinding
    lateinit var viewModel: LyricsViewModel
    lateinit var songModel: SongModel
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
        val args: SearchLyricsFragmentArgs by navArgs()
        songModel = args.lyricsDetail

        //viewModel
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory()
        ).get(LyricsViewModel::class.java)

        //set data
        binding.edtSong.setText(songModel.artist)
        binding.edtTitle.setText(songModel.songTitle)


        viewModel.lyrics.observe(requireActivity(), {
            binding.txtLyrics.text = it.lyrics
        })

        //search lyrics
        binding.btnSearch.setOnClickListener {
            if (binding.edtSong.text != null &&
                binding.edtTitle.text != null
            ) {
                viewModel.searchLyrics(
                    binding.edtSong.text.toString().trim(),
                    binding.edtSong.text.toString().trim()
                )
            }
        }
    }

}