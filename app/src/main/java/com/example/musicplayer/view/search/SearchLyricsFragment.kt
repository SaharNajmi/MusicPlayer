package com.example.musicplayer.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.databinding.FragmentLyricsSearchBinding
import com.example.musicplayer.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchLyricsFragment : Fragment() {
    lateinit var binding: FragmentLyricsSearchBinding
    lateinit var song: Song
    private val lyricsViewModel: LyricsViewModel by viewModels()

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

        //set data
        binding.edtSong.setText(song.artist)
        binding.edtTitle.setText(song.title)

        //show ProgressBar
        lyricsViewModel.loading.observe(viewLifecycleOwner, {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })

        //show lyrics in textView
        lyricsViewModel.lyrics.observe(requireActivity(), {
            if (it.lyrics.isNullOrEmpty()) {
                binding.txtLyrics.text = it.error
                binding.btnSearch.visibility = View.VISIBLE
                binding.btnApply.visibility = View.GONE
            } else {
                binding.txtLyrics.text = it.lyrics
                song.lyrics = it.lyrics.toString()
                binding.btnSearch.visibility = View.GONE
                binding.btnApply.visibility = View.VISIBLE
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