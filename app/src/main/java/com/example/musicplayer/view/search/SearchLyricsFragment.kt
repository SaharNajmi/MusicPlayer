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
        binding = FragmentLyricsSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: SearchLyricsFragmentArgs by navArgs()
        song = args.lyricsDetail
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.hideMusicController()
        }

        binding.edtSong.setText(song.artist)
        binding.edtTitle.setText(song.title)

        lyricsViewModel.loading.observe(viewLifecycleOwner, {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })

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

        binding.btnApply.setOnClickListener {
            lyricsViewModel.update(song)
            findNavController().navigate(
                SearchLyricsFragmentDirections.actionSearchLyricsFragmentToDetailFragment(
                    song
                )
            )
        }

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