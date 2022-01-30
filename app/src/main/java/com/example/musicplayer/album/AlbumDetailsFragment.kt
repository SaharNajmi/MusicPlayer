package com.example.musicplayer.album

import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.`interface`.SongEventListener
import com.example.model.AlbumModel
import com.example.model.SongModel
import com.example.musicplayer.databinding.FragmentAlbumDetailsBinding
import com.example.musicplayer.player.Player

class AlbumDetailsFragment : Fragment(), SongEventListener {
    lateinit var binding: FragmentAlbumDetailsBinding
    lateinit var albumModel: AlbumModel
    val args: AlbumDetailsFragmentArgs by navArgs()
    var albums = ArrayList<SongModel>()
    var musics = ArrayList<SongModel>()
    lateinit var myPlayer: Player

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAlbumDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        albumModel = args.albumDetail

        myPlayer = Player.getInstance()

        musics = myPlayer.getSongs(requireContext())

        //set data
        binding.albumTitle.text = albumModel.albumName
        binding.artist.text = albumModel.artist
        var bitmap: Bitmap? = null
        try {
            bitmap =
                MediaStore.Images.Media.getBitmap(
                    requireContext().contentResolver,
                    albumModel.albumImage
                )
            binding.imgCoverAlbum.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Log.e("Error set image!!", e.toString())
        }

        //Get list album items by albumId
        getAlbums()

        //show items
        initRecycler()
    }

    fun initRecycler() {
        binding.recyclerDetailAlbum.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerDetailAlbum.adapter = AlbumDetailAdapter(albums, this)
    }

    fun getAlbums() {
        val albumId = albumModel.id
        musics.forEach {
            if (albumId == it.albumID)
                albums.add(it)
        }
        myPlayer.musics = albums
    }

    override fun onSelect(songModel: SongModel, posSong: Int) {
        myPlayer.songSelected(songModel, posSong)
    }
}