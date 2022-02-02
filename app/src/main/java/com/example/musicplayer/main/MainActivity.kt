package com.example.musicplayer.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
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
    lateinit var myPlayer: Player
    var duration = 0
    lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val mNavController = findNavController(R.id.nav_host_fragment)

        myPlayer = Player.getInstance()

        //viewModel
        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(Player.getInstance())
        ).get(MainViewModel::class.java)

        val musics = viewModel.getMusics(this)

        musicController()

        updateUi(musics[viewModel.songPosition])

        //update ui music controller
        viewModel.songModel.observe(this, {
            songModel = it
            updateUi(it)
        })

        //update ui button pause or play music
        viewModel.playerState.observe(this, {
            updateUiPlayOrPause(it)
        })

        //get mediaPlayer duration
        /*     viewModel.duration.observe(this, {
                 duration = it
             })
     */
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
                    //search
                    is SearchMusicFragment -> mNavController.navigate(
                        SearchMusicFragmentDirections.actionSearchMusicFragmentToDetailFragment(
                            songModel
                        )
                    )
                }

            }
        }

        //update progressbar
        viewModel.progress.observe(this, {
            updateProgress(it)
        })
    }

    fun updateProgress(progress: Int) {
        //set progress to seekbar
        binding.playMusicLayout.seekBar.max = viewModel.getNewSongDuration()
        binding.playMusicLayout.seekBar.progress = progress
    }

    fun updateUi(song: SongModel) {
        Glide.with(this)
            .load(song.coverImage)
            .into(binding.playMusicLayout.coverImage)

        binding.playMusicLayout.songTitle.text = song.songTitle
        binding.playMusicLayout.artist.text = song.artist
    }

    fun updateUiPlayOrPause(playerState: PlayerState) {
        if (playerState == PlayerState.PAUSED)
            binding.playMusicLayout.btnPlayPause.setImageResource(R.drawable.ic_play)
        else if (playerState == PlayerState.PLAYING)
            binding.playMusicLayout.btnPlayPause.setImageResource(R.drawable.ic_pause)
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
            viewModel.toggleState()
        }

        //next song
        binding.playMusicLayout.btnNext.setOnClickListener {
            viewModel.nextSong()
        }

        //previous song
        binding.playMusicLayout.btnBack.setOnClickListener {
            viewModel.backSong()
        }
    }

}