package com.example.musicplayer.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.musicplayer.data.db.MusicDatabase
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.data.repository.LocalMusic
import com.example.musicplayer.data.repository.MusicRepository
import com.example.musicplayer.databinding.FragmentLyricsSearchBinding
import com.example.musicplayer.factory.MainViewModelFactory
import com.example.musicplayer.view.main.MainActivity

class SearchLyricsFragment : Fragment() {
    lateinit var binding: FragmentLyricsSearchBinding
    lateinit var song: Song
    lateinit var lyricsViewModel: LyricsViewModel

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
        song = args.lyricsDetail
        // hide music controller
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.hideMusicController()
        }

        //search lyrics viewModel
        val musicDao = MusicDatabase.getInstance(requireContext()).musicDao()
        lyricsViewModel =
            ViewModelProvider(
                this,
                MainViewModelFactory(
                    MusicRepository(LocalMusic(requireContext()), musicDao)
                )
            ).get(LyricsViewModel::class.java)

        //set data
        binding.edtSong.setText(song.artist)
        binding.edtTitle.setText(song.songTitle)


        //show lyrics in textView
        lyricsViewModel.lyrics.observe(requireActivity(), {
            if (it.lyrics != null) {
                binding.txtLyrics.text = it.lyrics
                song.lyrics = it.lyrics.toString()
            } else
                binding.txtLyrics.text = it.error
        })

        //find lyrics
        lyricsViewModel.findLyrics.observe(requireActivity(), { find ->
            if (find) {
                binding.btnSearch.visibility = View.GONE
                binding.btnApply.visibility = View.VISIBLE
            } else {
                binding.btnSearch.visibility = View.VISIBLE
                binding.btnApply.visibility = View.GONE
            }
        })

        //search lyrics
        binding.btnSearch.setOnClickListener {
            if (binding.edtSong.text != null &&
                binding.edtTitle.text != null
            ) {
                lyricsViewModel.searchLyrics(
                    binding.edtSong.text.toString().trim(),
                    binding.edtSong.text.toString().trim()
                )
            }
        }

        //apply lyrics
        binding.btnApply.setOnClickListener {
            //update lyrics
            lyricsViewModel.update(song)
            findNavController().navigate(
                SearchLyricsFragmentDirections.actionSearchLyricsFragmentToDetailFragment(
                    song
                )
            )
        }

        //go back
        binding.btnBackPage.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.btnSearch.visibility = View.VISIBLE
        binding.btnApply.visibility = View.GONE
    }
}