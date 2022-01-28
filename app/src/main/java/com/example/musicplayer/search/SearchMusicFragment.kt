package com.example.musicplayer.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.`interface`.SongEventListener
import com.example.model.SongModel
import com.example.musicplayer.all.SongAdapter
import com.example.musicplayer.databinding.FragmentSearchMusicBinding
import com.example.musicplayer.player.Player

class SearchMusicFragment : Fragment(), SongEventListener {
    lateinit var binding: FragmentSearchMusicBinding
    var listMusic = ArrayList<SongModel>()
    lateinit var adapter: SongAdapter


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
        //get all songs
        listMusic = Player.getListSong(requireContext())

        //search item
        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                searchSong(p0.toString())
            }
        })

        //show all song
        showListMusic()
    }

    fun showListMusic() {
        adapter = SongAdapter(requireContext(), listMusic, this)
        binding.recyclerShowItemSearch.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerShowItemSearch.adapter = adapter
    }

    fun searchSong(value: String) {
        val listFilterSong = ArrayList<SongModel>()
        for (song in listMusic) {
            var isListAdded = false
            if (song.songTitle.toLowerCase().contains(value.toLowerCase())) {
                listFilterSong.add(song)
                isListAdded = true
            }
            if (song.artist.toLowerCase().contains(value.toLowerCase())) {
                if (!isListAdded)
                    listFilterSong.add(song)
            }
        }
        Player.listMusic = listFilterSong
        adapter.updateList(listFilterSong)
    }

    override fun onSelect(songModel: SongModel, posSong: Int) {
        Player.setSong(songModel, posSong)
    }
}