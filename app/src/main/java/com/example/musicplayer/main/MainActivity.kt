package com.example.musicplayer.main

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.model.SongModel
import com.example.musicplayer.R
import com.example.musicplayer.album.AlbumDetailFragment
import com.example.musicplayer.album.AlbumDetailFragmentDirections
import com.example.musicplayer.artist.ArtistDetailFragment
import com.example.musicplayer.artist.ArtistDetailFragmentDirections
import com.example.musicplayer.databinding.ActivityMainBinding
import com.example.musicplayer.file.FileDetailFragment
import com.example.musicplayer.file.FileDetailFragmentDirections
import com.example.musicplayer.player.PlayerState
import com.example.musicplayer.search.SearchMusicFragment
import com.example.musicplayer.search.SearchMusicFragmentDirections
import com.example.room.MusicViewModel

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var duration = 0
    lateinit var viewModel: MainViewModel
    lateinit var musicViewModel: MusicViewModel
    lateinit var editer: SharedPreferences.Editor
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val mNavController = findNavController(R.id.nav_host_fragment)

        //sharedPreferences
        sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE)
        editer = sharedPreferences.edit()

        //main viewModel
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory()
        ).get(MainViewModel::class.java)

        //music viewModel
        /*  val dao = MusicDatabase.getInstance(this).musicDao()
                 musicViewModel =
                     ViewModelProvider(
                         this,
                         MusicViewModelFactory(MusicRepository(dao))
                     ).get(MusicViewModel::class.java)*/

        //save old data with sharedPreferences
        shaowSaveState()

        musicController()

        //update ui music controller
        viewModel.songModel.observe(this, {
            updateUi(it)
        })

        //update ui button pause or play music
        updateUiPlayOrPause(PlayerState.PAUSED)
        viewModel.playerState.observe(this, {
            updateUiPlayOrPause(it)
        })

        //go detail music
        binding.playMusicLayout.layoutController.setOnClickListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            fragment?.childFragmentManager?.fragments?.forEach { frg ->
                when (frg) {
                    //home
                    is MainFragment -> mNavController.navigate(
                        MainFragmentDirections.actionMainFragmentToDetailFragment(viewModel.songModel.value!!)
                    )
                    //album
                    is AlbumDetailFragment -> mNavController.navigate(
                        AlbumDetailFragmentDirections.actionAlbumDetailsFragmentToDetailFragment(
                            viewModel.songModel.value!!
                        )
                    )
                    //artist
                    is ArtistDetailFragment -> mNavController.navigate(
                        ArtistDetailFragmentDirections.actionArtistDetailFragmentToDetailFragment(
                            viewModel.songModel.value!!
                        )
                    )
                    //folder
                    is FileDetailFragment -> mNavController.navigate(
                        FileDetailFragmentDirections.actionFileDetailFragmentToDetailFragment(
                            viewModel.songModel.value!!
                        )
                    )
                    //search
                    is SearchMusicFragment -> mNavController.navigate(
                        SearchMusicFragmentDirections.actionSearchMusicFragmentToDetailFragment(
                            viewModel.songModel.value!!
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

    private fun shaowSaveState() {
        var oldPositionSong = sharedPreferences.getInt("position", 0)
        //when first play application
        if (oldPositionSong < 0)
            oldPositionSong = 0

        //update ui music controller
        val musics = viewModel.getMusics(this)
        if (musics.size != 0 && oldPositionSong != 0)
            updateUi(musics[oldPositionSong])

        //update progress
        //get old progress/duration song
        val oldProgressSong = sharedPreferences.getInt("progress", 0)
        val oldDurationSong = sharedPreferences.getInt("duration", 0)
        updateProgress(oldProgressSong, oldDurationSong)

        //Change default values in Player
        viewModel.changeSongModel(musics[oldPositionSong])
        viewModel.changeSongPosition(oldPositionSong)
    }

    override fun onPause() {
        super.onPause()
        //save values
        editer.apply {
            putInt("position", viewModel.getSongPosition())
            putInt("progress", viewModel.getValueProgress()!!)
            putInt("duration", viewModel.getDuration())
            commit()
        }
    }

    fun updateProgress(progress: Int) {
        //set progress to seekbar
        binding.playMusicLayout.seekBar.max = viewModel.getNewSongDuration()
        binding.playMusicLayout.seekBar.progress = progress
    }

    fun updateProgress(progress: Int, duration: Int) {
        binding.playMusicLayout.seekBar.max = duration
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
        binding.playMusicLayout.btnPlayPause.setOnClickListener {
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