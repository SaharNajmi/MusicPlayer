package com.example.musicplayer.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.`interface`.AlbumEventListener
import com.example.model.AlbumModel
import com.example.musicplayer.ReadExternalMusic
import com.example.musicplayer.databinding.FragmentAlbumBinding
import com.example.musicplayer.main.MainFragmentDirections
import com.example.musicplayer.player.Player

class AlbumFragment : Fragment(), AlbumEventListener {
    lateinit var binding: FragmentAlbumBinding
    var albums = ArrayList<AlbumModel>()
    lateinit var myPlayer: Player

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myPlayer = Player.getInstance()

        //get array list album
        val musics = myPlayer.getSongs(requireContext())
        val albumIDs = ReadExternalMusic().getAlbumIDs(musics)
        albums = ReadExternalMusic().getAlbums(musics, albumIDs)

        //show list album
        showAlbums()
    }

    fun showAlbums() {
        binding.recyclerAlbum.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        binding.recyclerAlbum.adapter = AlbumAdapter(requireContext(), albums, this)
    }

    override fun onSelect(albumModel: AlbumModel) {
        val directions = MainFragmentDirections.actionMainFragmentToAlbumDetailsFragment(albumModel)
        findNavController().navigate(directions)
    }

}