package com.example.musicplayer.view.main

import android.app.Notification
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.musicplayer.R
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.databinding.ActivityMainBinding
import com.example.musicplayer.factory.BaseViewModelFactory
import com.example.musicplayer.service.ForegroundService
import com.example.musicplayer.service.NotificationReceiver
import com.example.musicplayer.utils.ActionPlaying
import com.example.musicplayer.utils.Constants
import com.example.musicplayer.utils.PlayerState
import com.example.musicplayer.view.album.AlbumDetailFragment
import com.example.musicplayer.view.album.AlbumDetailFragmentDirections
import com.example.musicplayer.view.artist.ArtistDetailFragment
import com.example.musicplayer.view.artist.ArtistDetailFragmentDirections
import com.example.musicplayer.view.favorite.FavoriteFragment
import com.example.musicplayer.view.favorite.FavoriteFragmentDirections
import com.example.musicplayer.view.file.FileDetailFragment
import com.example.musicplayer.view.file.FileDetailFragmentDirections
import com.example.musicplayer.view.search.SearchMusicFragment
import com.example.musicplayer.view.search.SearchMusicFragmentDirections


class MainActivity : AppCompatActivity(), ServiceConnection, ActionPlaying {
    lateinit var binding: ActivityMainBinding
    var duration = 0
    lateinit var viewModel: MainViewModel
    lateinit var editor: SharedPreferences.Editor
    lateinit var sharedPreferences: SharedPreferences
    var musicService: ForegroundService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val mNavController = findNavController(R.id.nav_host_fragment)

        //sharedPreferences
        sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE)
        editor = sharedPreferences.edit()

        //bind service
        val intent = Intent(this, ForegroundService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)

        //main viewModel
        viewModel = ViewModelProvider(
            this,
            BaseViewModelFactory(this)
        ).get(MainViewModel::class.java)

        //save old data with sharedPreferences
        shaowSaveState()

        musicController()

        //update ui music controller
        viewModel.songModel.observe(this, {
            updateUi(it)
            showNotification(it, R.drawable.ic_pause)
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
                    //favorite
                    is FavoriteFragment -> mNavController.navigate(
                        FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(
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
        editor.apply {
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
        if (playerState == PlayerState.PAUSED) {
            showNotification(viewModel.songModel.value!!, R.drawable.ic_play)
            binding.playMusicLayout.btnPlayPause.setImageResource(R.drawable.ic_play)
        } else if (playerState == PlayerState.PLAYING) {
            showNotification(viewModel.songModel.value!!, R.drawable.ic_pause)
            binding.playMusicLayout.btnPlayPause.setImageResource(R.drawable.ic_pause)
        }
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

    override fun onServiceConnected(p0: ComponentName?, iBinder: IBinder?) {
        val binder: ForegroundService.MusicBinder = iBinder as ForegroundService.MusicBinder
        musicService = binder.getService()
        musicService!!.setCallBack(this)
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        musicService = null
    }

    private fun showNotification(song: SongModel, playPause: Int) {
        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            notificationIntent, 0
        )

        //play action
        val playIntent =
            Intent(this, NotificationReceiver::class.java).setAction(Constants.PLAY_ACTION)
        val pplayIntent = PendingIntent.getBroadcast(
            this, 0,
            playIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        //next action
        val nextIntent =
            Intent(this, NotificationReceiver::class.java).setAction(Constants.NEXT_ACTION)
        val pnextIntent = PendingIntent.getBroadcast(
            this, 0,
            nextIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        //close action
        val closeIntent =
            Intent(
                this,
                NotificationReceiver::class.java
            ).setAction(Constants.STOPFOREGROUND_ACTION)
        val pcloseIntent = PendingIntent.getBroadcast(
            this, 0,
            closeIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        //create notification
        val notification: Notification = NotificationCompat.Builder(this)
            .setContentTitle(song.songTitle)
            .setContentText(song.artist)
            .setSmallIcon(R.drawable.music_note)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .addAction(
                playPause, null,
                pplayIntent
            )
            .addAction(
                R.drawable.ic_next, null,
                pnextIntent
            )
            .addAction(
                R.drawable.ic_close, null,
                pcloseIntent
            ).build()
        //start foregroundService
        musicService?.startForeground(
            1,
            notification
        )
    }

    override fun nextClicked() {
        viewModel.nextSong()
    }

    override fun playClicked() {
        viewModel.toggleState()
    }

    override fun closeClicked() {
        viewModel.pauseSong()
    }
}