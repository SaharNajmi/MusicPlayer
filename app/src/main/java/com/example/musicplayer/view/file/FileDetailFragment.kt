package com.example.musicplayer.view.file

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.databinding.FragmentFileDetailBinding
import com.example.musicplayer.view.all.SongAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FileDetailFragment : Fragment(), SongAdapter.SongEventListener {
    lateinit var binding: FragmentFileDetailBinding
    val args: FileDetailFragmentArgs by navArgs()
    private val viewModel: FileDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFileDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.folderName.text = args.fileName

        initRecycler()
    }

    private fun initRecycler() {
        binding.recyclerDetailFolder.layoutManager = LinearLayoutManager(requireContext())
        viewModel.getMusics(args.fileName).observe(viewLifecycleOwner, { list ->
            binding.recyclerDetailFolder.adapter =
                SongAdapter(requireContext(), list, this)
        })
    }

    override fun onSelect(song: Song, posSong: Int) {
        viewModel.songSelected(song, posSong)
    }
}