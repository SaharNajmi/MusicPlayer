package com.example.musicplayer.view.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.databinding.FragmentSearchMusicBinding
import com.example.musicplayer.view.all.SongAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchMusicFragment : Fragment(), SongAdapter.SongEventListener {
    lateinit var binding: FragmentSearchMusicBinding
    lateinit var adapter: SongAdapter
    private val viewModel: SearchMusicViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showMusics()

        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(text: Editable?) {
                viewModel.searchSong(text.toString(), adapter)
            }
        })

        binding.btnBackPage.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    fun showMusics() {
        adapter = SongAdapter(requireContext(), arrayListOf(), this)
        binding.recyclerShowItemSearch.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerShowItemSearch.adapter = adapter
    }

    override fun onSelect(song: Song, posSong: Int) {
        viewModel.songSelected(song, posSong)
    }
}