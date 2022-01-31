package com.example.musicplayer.main

import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.model.SongModel
import com.example.musicplayer.R
import com.example.musicplayer.album.AlbumDetailsFragment
import com.example.musicplayer.album.AlbumDetailsFragmentDirections
import com.example.musicplayer.artist.ArtistDetailFragment
import com.example.musicplayer.artist.ArtistDetailFragmentDirections
import com.example.musicplayer.databinding.ActivityMainBinding
import com.example.musicplayer.file.FileDetailFragment
import com.example.musicplayer.file.FileDetailFragmentDirections
import com.example.musicplayer.player.Player
import com.example.musicplayer.player.PlayerState
import com.example.musicplayer.search.SearchMusicFragment
import com.example.musicplayer.search.SearchMusicFragmentDirections

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var songModel: SongModel
    var musics = ArrayList<SongModel>()
    private lateinit var mNavController: NavController
    lateinit var myPlayer: Player
    var playerState = PlayerState.PAUSED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        myPlayer = Player.getInstance()

        musics = myPlayer.getSongs(this)

        mNavController = findNavController(R.id.nav_host_fragment)

        musicController()

        updateUi(musics[myPlayer.songPosition])

        //update ui music controller
        myPlayer.songModel.observe(this, {
            songModel = it
            updateUi(it)
            myPlayer.playSong(songModel.id, applicationContext)
        })

        //update ui button pause or play music
        myPlayer.playerState.observe(this, {
            playerState = it
            updateUiPlayOrPause(it)
        })

        //go detail music
        binding.playMusicLayout.layoutController.setOnClickListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            fragment?.childFragmentManager?.fragments?.forEach { frg ->
                when (frg) {
                    //home
                    is MainFragment -> mNavController.navigate(
                        MainFragmentDirections.actionMainFragmentToDetailFragment(songModel)
                    )
                    //album
                    is AlbumDetailsFragment -> mNavController.navigate(
                        AlbumDetailsFragmentDirections.actionAlbumDetailsFragmentToDetailFragment(
                            songModel
                        )
                    )
                    //artist
                    is ArtistDetailFragment -> mNavController.navigate(
                        ArtistDetailFragmentDirections.actionArtistDetailFragmentToDetailFragment(
                            songModel
                        )
                    )
                    //folder
                    is FileDetailFragment -> mNavController.navigate(
                        FileDetailFragmentDirections.actionFileDetailFragmentToDetailFragment(
                            songModel
                        )
                    )
                    //serach
                    is SearchMusicFragment -> mNavController.navigate(
                        SearchMusicFragmentDirections.actionSearchMusicFragmentToDetailFragment(
                            songModel
                        )
                    )
                }

            }
        }

        //update progressbar
        myPlayer.progress.observe(this, {
            updateProgress(it)
        })
    }

    fun updateProgress(progress: Int) {
        //set progress to seekbar
        binding.playMusicLayout.seekBar.max = myPlayer.duration
        binding.playMusicLayout.seekBar.progress = progress
        //run again after 1 second
        if (playerState == PlayerState.PLAYING)
            binding.playMusicLayout.seekBar.postDelayed(myPlayer.progressRunner(), 1000)
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
    }

    fun updateUiPlayOrPause(playerState: PlayerState) {
        if (playerState == PlayerState.PAUSED)
            binding.playMusicLayout.btnPlayPause.setImageResource(R.drawable.ic_play)
        else if (playerState == PlayerState.PLAYING)
            binding.playMusicLayout.btnPlayPause.setImageResource(R.drawable.ic_pause)
    }

    fun playSong() {
        if (playerState == PlayerState.PAUSED)
            myPlayer.resumeSong()
        else if (playerState == PlayerState.PLAYING)
            myPlayer.pauseSong()
    }

    fun hideMusicController() {
        binding.playMusicLayout.layoutController.visibility = View.GONE
    }

    fun showMusicController() {
        binding.playMusicLayout.layoutController.visibility = View.VISIBLE
    }

    fun musicController() {
        //play and pause song
        val btnPlayPause = binding.playMusicLayout.btnPlayPause

        btnPlayPause.setOnClickListener {
            playSong()
        }

        //next song
        binding.playMusicLayout.btnNext.setOnClickListener {
            myPlayer.nextSong(this)
        }

        //previous song
        binding.playMusicLayout.btnBack.setOnClickListener {
            myPlayer.backSong(this)
        }
    }

}