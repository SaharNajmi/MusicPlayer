package com.example.musicplayer.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.model.SongModel
import com.example.musicplayer.all.SongAdapter
import com.example.musicplayer.databinding.FragmentSearchMusicBinding
import com.example.musicplayer.main.ViewModelFactory
import com.example.musicplayer.player.Player
import com.example.myInterface.SongEventListener

class
SearchMusicFragment : Fragment(), SongEventListener {
    lateinit var binding: FragmentSearchMusicBinding
    lateinit var adapter: SongAdapter
    lateinit var viewModel: SearchMusicViewModel

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

        //viewModel
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory()
        ).get(SearchMusicViewModel::class.java)

        //show all song
        showMusics()

        //search item
        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.searchSong(p0.toString(), requireContext(), adapter)
            }
        })

        // go back
        binding.btnBackPage.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    fun showMusics() {
        adapter = SongAdapter(requireContext(), viewModel.getMusics(requireContext()), this)
        binding.recyclerShowItemSearch.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerShowItemSearch.adapter = adapter
    }

    override fun onSelect(songModel: SongModel, posSong: Int) {
        Player.getInstance().songSelected(songModel, posSong)
    }
}