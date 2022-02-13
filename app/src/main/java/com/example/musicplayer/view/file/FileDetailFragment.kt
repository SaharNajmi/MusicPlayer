package com.example.musicplayer.view.file

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.databinding.FragmentFileDetailBinding
import com.example.musicplayer.factory.BaseViewModelFactory
import com.example.musicplayer.player.Player
import com.example.musicplayer.view.all.SongAdapter

class FileDetailFragment : Fragment(), SongAdapter.SongEventListener {
    lateinit var binding: FragmentFileDetailBinding
    val args: FileDetailFragmentArgs by navArgs()
    lateinit var viewModel: FileDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFileDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //set Text textView FolderName
        binding.folderName.text = args.fileDetail

        //viewModel
        viewModel = ViewModelProvider(
            requireActivity(),
            BaseViewModelFactory(requireContext())
        ).get(FileDetailViewModel::class.java)

        //show items
        initRecycler()
    }

    fun initRecycler() {
        val musics = viewModel.getMusics(requireContext())
        //Get list folder items by folderName
        val list = viewModel.getMusicsInsideFolder(args.fileDetail, musics)

        binding.recyclerDetailFolder.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerDetailFolder.adapter =
            SongAdapter(requireContext(), list, this)
    }

    override fun onSelect(songModel: SongModel, posSong: Int) {
        Player.getInstance(requireContext()).songSelected(songModel, posSong)
    }
}