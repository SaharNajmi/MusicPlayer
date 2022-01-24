package com.example.musicplayer.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.os.Bundle
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.`interface`.OnSongComplete
import com.example.model.SongModel
import com.example.musicplayer.R
import com.example.musicplayer.databinding.ActivityMainBinding
import com.example.musicplayer.player.Player
import com.example.service.MusicService

class MainActivity : AppCompatActivity(), OnSongComplete {
    lateinit var binding: ActivityMainBinding
    var playIntent: Intent? = null
    var musicBound = false
    lateinit var songModel: SongModel
    lateinit var musicService: MusicService
    var listMusic = ArrayList<SongModel>()
    private lateinit var mNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        listMusic = Player.getListSong(this)

        mNavController = findNavController(R.id.nav_host_fragment)

        musicController()

        updateUi(listMusic[Player.songPosition])

        //update ui music controller
        Player.liveDataSongModel.observe(this, {
            songModel = it
            updateUi(it)
            Player.playSong(songModel.id, applicationContext)
        })

        Player.liveDataPlayerState.observe(this, {
            if (it == 1) {
                binding.playMusicLayout.btnPlayPause.setImageResource(R.drawable.ic_play)
            } else if (it == 2) {
                binding.playMusicLayout.btnPlayPause.setImageResource(R.drawable.ic_pause)
            }
        })

        //go detail music
        binding.playMusicLayout.layoutController.setOnClickListener {
            val directions = MainFragmentDirections.actionMainFragmentToDetailFragment(songModel)
            mNavController.navigate(directions)

            /* val directions =
                 AlbumDetailsFragmentDirections.actionAlbumDetailsFragmentToDetailFragment(songModel)
             mNavController.navigate(directions)*/
        }

    }

    override fun onStart() {
        super.onStart()
        if (playIntent == null) {
            //bind service
            playIntent = Intent(this, MusicService::class.java)
            bindService(
                playIntent,
                boundServiceConnection,
                Context.BIND_AUTO_CREATE
            )
            //start service
            startService(playIntent)
        }
    }

    fun updateUi(song: SongModel) {
        var bitmap: Bitmap? = null
        try {
            bitmap =
                MediaStore.Images.Media.getBitmap(
                    contentResolver,
                    song.coverImage
                )
            binding.playMusicLayout.coverImage.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Log.e("Error set image!!", e.toString())
        }
        binding.playMusicLayout.songTitle.text = song.songTitle
        binding.playMusicLayout.artist.text = song.artist

        updateUiPlayOrPause()
    }

    fun updateUiPlayOrPause() {
        if (Player.liveDataPlayerState.value == 1) {
            binding.playMusicLayout.btnPlayPause.setImageResource(R.drawable.ic_pause)
        } else if (Player.liveDataPlayerState.value == 2) {
            binding.playMusicLayout.btnPlayPause.setImageResource(R.drawable.ic_play)
        }
    }

    fun hideMusicController() {
        binding.playMusicLayout.layoutController.visibility = View.GONE
    }

    fun showMusicController() {
        binding.playMusicLayout.layoutController.visibility = View.VISIBLE
    }

    private val boundServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName?, iBinder: IBinder?) {
            val binder: MusicService.MusicBinder = iBinder as MusicService.MusicBinder
            musicService = binder.getService()

            //end song
            musicService.songComplete(this@MainActivity)

            musicBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            musicBound = false
        }

    }

    fun playSong(playBtn: ImageView) {
        if (Player.liveDataPlayerState.value == 1) {
            playBtn.setImageResource(R.drawable.ic_pause)
            Player.resumeSong()
        } else if (Player.liveDataPlayerState.value == 2) {
            playBtn.setImageResource(R.drawable.ic_play)
            Player.pauseSong()
        }
    }

    fun musicController() {
        //play and pause song
        val btnPlayPause = binding.playMusicLayout.btnPlayPause

        btnPlayPause.setOnClickListener {
            playSong(btnPlayPause)
        }

        //next song
        binding.playMusicLayout.btnNext.setOnClickListener {
            Player.nextSong(this)
        }

        //previous song
        binding.playMusicLayout.btnBack.setOnClickListener {
            Player.backSong(this)
        }
    }

    override fun onSongComplete() {
        when {
            //repeat is on
            Player.isRepeat -> Player.repeatSong()

            //shuffle is on
            Player.isShuffle -> Player.shuffleSong()

            //next song
            else -> Player.nextSong(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        /* stopService(playIntent)
         unbindService(boundServiceConnection)*/
    }
}