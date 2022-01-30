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
    var musics = ArrayList<SongModel>()
    lateinit var adapter: SongAdapter
    lateinit var myPlayer: Player


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

        myPlayer = Player.getInstance()

        //get all songs
        musics = myPlayer.getSongs(requireContext())

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
        showMusics()
    }

    fun showMusics() {
        adapter = SongAdapter(requireContext(), musics, this)
        binding.recyclerShowItemSearch.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerShowItemSearch.adapter = adapter
    }

    fun searchSong(value: String) {
        val filterSongs = ArrayList<SongModel>()
        for (song in musics) {
            var isListAdded = false
            if (song.songTitle.toLowerCase().contains(value.toLowerCase())) {
                filterSongs.add(song)
                isListAdded = true
            }
            if (song.artist.toLowerCase().contains(value.toLowerCase())) {
                if (!isListAdded)
                    filterSongs.add(song)
            }
        }
        myPlayer.musics = filterSongs
        adapter.updateList(filterSongs)
    }

    override fun onSelect(songModel: SongModel, posSong: Int) {
        myPlayer.songSelected(songModel, posSong)
    }
}