package com.example.musicplayer

import android.content.*
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.`interface`.CategoryEventListener
import com.example.`interface`.SongEventListener
import com.example.adapter.CategoryAdapter
import com.example.adapter.SongAdapter
import com.example.common.Constants.POSITION_SONG
import com.example.musicplayer.databinding.FragmentHomeBinding
import com.model.Category
import com.model.SongModel

class HomeFragment : Fragment(), SongEventListener, CategoryEventListener {
    lateinit var binding: FragmentHomeBinding
    lateinit var musicService: MusicService
    var playIntent: Intent? = null
    var musicBound = false
    lateinit var songModel: SongModel
    val listMusic = ArrayList<SongModel>()
    val listCategory = listOf(
        Category.All,
        Category.Folder,
        Category.Album,
        Category.Artist
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //go to favorite frg
        binding.favorite.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_favoriteFragment)
        }
        //go to search music frg
        binding.search.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_searchMusicFragment)
        }

        //get all songs from phone
        readExternalData()

        //show category
        showListCategory()

        //show list items into recyclerView
        showListMusic()

        //buttons control song
        musicController()
    }

    private val boundServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName?, iBinder: IBinder?) {
            val binder: MusicService.MusicBinder = iBinder as MusicService.MusicBinder
            musicService = binder.getService()
            musicBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            musicBound = false
        }

    }

    override fun onStart() {
        super.onStart()
        if (playIntent == null) {
            //bind service
            playIntent = Intent(requireContext(), MusicService::class.java)
            requireContext().bindService(
                playIntent,
                boundServiceConnection,
                Context.BIND_AUTO_CREATE
            )
            //start service
            requireContext().startService(playIntent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireContext().stopService(playIntent)
        requireContext().unbindService(boundServiceConnection)
    }

    fun showListCategory() {
        binding.recyclerCategory.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.recyclerCategory.adapter = CategoryAdapter(listCategory, this)
    }

    fun showListMusic() {
        binding.recyclerMusics.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMusics.adapter = SongAdapter(requireContext(), listMusic, this)
    }

    fun readExternalData() {
        var musicResolver: ContentResolver = requireContext().contentResolver
        val musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val musicCursor = musicResolver.query(musicUri, null, null, null, null)
        if (musicCursor != null && musicCursor.moveToFirst()) {
            val songId = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val songArtist = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val songTitle = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val albumId = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
            while (musicCursor.moveToNext()) {
                //get cover image song
                val IMAGE_URI = Uri.parse("content://media/external/audio/albumart")
                val album_uri =
                    ContentUris.withAppendedId(IMAGE_URI, musicCursor.getLong(albumId))

                //add music into array song
                listMusic.add(
                    SongModel(
                        musicCursor.getLong(songId),
                        musicCursor.getString(songArtist),
                        musicCursor.getString(songTitle),
                        album_uri
                    )
                )
            }
        }
    }

    fun updateUi() {
        var bitmap: Bitmap? = null
        try {
            bitmap =
                MediaStore.Images.Media.getBitmap(
                    requireContext().contentResolver,
                    songModel.coverImage
                )
            binding.playMusicLayout.coverImage.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Log.e("Error set image!!", e.toString())
        }
        binding.playMusicLayout.songTitle.text = songModel.songTitle
        binding.playMusicLayout.artist.text = songModel.artist
    }

    fun musicController() {
        //play and pause song
        val btnPlayPause = binding.playMusicLayout.btnPlayPause
        btnPlayPause.setOnClickListener {
            if (musicService.playerState == 1) {
                btnPlayPause.setImageResource(R.drawable.ic_pause)
                musicService.resumeSong()
            } else if (musicService.playerState == 2) {
                btnPlayPause.setImageResource(R.drawable.ic_play)
                musicService.pauseSong()
            }
        }

        //next song
        binding.playMusicLayout.btnNext.setOnClickListener {
            if (listMusic.size > 0) {
                if (POSITION_SONG != -1) {
                    if (listMusic.size - 1 == POSITION_SONG) {
                        POSITION_SONG = 0
                        musicService.setSong(listMusic[POSITION_SONG])
                        songModel = listMusic[POSITION_SONG]
                        updateUi()
                    } else {
                        ++POSITION_SONG
                        musicService.setSong(listMusic[POSITION_SONG])
                        songModel = listMusic[POSITION_SONG]
                        updateUi()
                    }
                }
            }
        }

        //previous song
        binding.playMusicLayout.btnBack.setOnClickListener {
            if (POSITION_SONG != -1) {
                if (POSITION_SONG == 0) {
                    POSITION_SONG = listMusic.size - 1
                    musicService.setSong(listMusic[POSITION_SONG])
                    songModel = listMusic[POSITION_SONG]
                    updateUi()
                } else {
                    POSITION_SONG--
                    musicService.setSong(listMusic[POSITION_SONG])
                    songModel = listMusic[POSITION_SONG]
                    updateUi()
                }
            }
        }
    }

    override fun onSelect(songModel: SongModel) {
        this.songModel = songModel
        musicService.setSong(songModel)
        updateUi()
    }

    override fun onItemClickListener(category: Category) {
        Toast.makeText(requireContext(), category.name, Toast.LENGTH_SHORT).show()
    }

}