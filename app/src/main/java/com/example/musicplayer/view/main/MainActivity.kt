package com.example.musicplayer.view.main

import android.app.Notification
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.musicplayer.R
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.databinding.ActivityMainBinding
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ServiceConnection, ActionPlaying {
    lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    var musicService: ForegroundService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val mNavController = findNavController(R.id.nav_host_fragment)

        val intent = Intent(this, ForegroundService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)

        viewModel.databaseExists.observe(this, { exists ->
            if (!exists) {
                viewModel.insertMusics()
                viewModel.insertAlbums()
                viewModel.insertArtists()
            }
        })

        showSaveState()

        musicController()

        viewModel.song.observe(this, {
            updateUi(it)
            showNotification(it, R.drawable.ic_pause)
        })

        viewModel.playerState.observe(this, {
            updateUiPlayOrPause(it)
        })

        binding.playMusicLayout.layoutController.setOnClickListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            fragment?.childFragmentManager?.fragments?.forEach { frg ->
                when (frg) {
                    is MainFragment -> mNavController.navigate(
                        MainFragmentDirections.actionMainFragmentToDetailFragment(viewModel.song.value!!)
                    )
                    is AlbumDetailFragment -> mNavController.navigate(
                        AlbumDetailFragmentDirections.actionAlbumDetailsFragmentToDetailFragment(
                            viewModel.song.value!!
                        )
                    )
                    is ArtistDetailFragment -> mNavController.navigate(
                        ArtistDetailFragmentDirections.actionArtistDetailFragmentToDetailFragment(
                            viewModel.song.value!!
                        )
                    )
                    is FileDetailFragment -> mNavController.navigate(
                        FileDetailFragmentDirections.actionFileDetailFragmentToDetailFragment(
                            viewModel.song.value!!
                        )
                    )
                    is SearchMusicFragment -> mNavController.navigate(
                        SearchMusicFragmentDirections.actionSearchMusicFragmentToDetailFragment(
                            viewModel.song.value!!
                        )
                    )
                    is FavoriteFragment -> mNavController.navigate(
                        FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(
                            viewModel.song.value!!
                        )
                    )
                }

            }
        }

        viewModel.progress.observe(this, {
            updateProgress(it)
        })
    }

    private fun showSaveState() {
        var oldPositionSong = viewModel.getSharedPreference("position", 0)
        val oldProgressSong = viewModel.getSharedPreference("progress", 0)
        val oldDurationSong = viewModel.getSharedPreference("duration", 0)

        if (oldPositionSong < 0)
            oldPositionSong = 0

        viewModel.musics.observe(this, { list ->
            if (list.isNotEmpty() && oldPositionSong != 0) {
                updateUi(list[oldPositionSong])

                viewModel.changeSong(list[oldPositionSong])
            }
        })

        updateProgress(oldProgressSong, oldDurationSong)

        viewModel.changeSongPosition(oldPositionSong)
    }

    override fun onPause() {
        super.onPause()
        viewModel.setSharedPreference("position", viewModel.songPosition)
        viewModel.setSharedPreference("progress", viewModel.progress.value!!)
        viewModel.setSharedPreference("duration", viewModel.song.value!!.duration)
    }

    private fun updateProgress(progress: Int) {
        viewModel.song.observe(this) { song ->
            binding.playMusicLayout.seekBar.max = song.duration
        }
        binding.playMusicLayout.seekBar.progress = progress
    }

    private fun updateProgress(progress: Int, duration: Int) {
        binding.playMusicLayout.seekBar.max = duration
        binding.playMusicLayout.seekBar.progress = progress
    }

    fun updateUi(song: Song) {
        Glide.with(this)
            .load(song.coverImage)
            .into(binding.playMusicLayout.coverImage)

        binding.playMusicLayout.songTitle.text = song.title
        binding.playMusicLayout.artist.text = song.artist
    }

    private fun updateUiPlayOrPause(playerState: PlayerState) {
        if (playerState == PlayerState.PAUSED) {
            viewModel.song.observe(this, {
                if (it != null)
                    showNotification(viewModel.song.value!!, R.drawable.ic_play)
            })
            binding.playMusicLayout.btnPlayPause.setImageResource(R.drawable.ic_play)
        } else if (playerState == PlayerState.PLAYING) {
            showNotification(viewModel.song.value!!, R.drawable.ic_pause)
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
        binding.playMusicLayout.btnPlayPause.setOnClickListener {
            viewModel.toggleState()
        }

        binding.playMusicLayout.btnNext.setOnClickListener {
            viewModel.nextSong()
        }

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

    private fun showNotification(song: Song, playPause: Int) {
        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            notificationIntent, 0
        )

        val playIntent =
            Intent(this, NotificationReceiver::class.java).setAction(Constants.PLAY_ACTION)
        val pplayIntent = PendingIntent.getBroadcast(
            this, 0,
            playIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val nextIntent =
            Intent(this, NotificationReceiver::class.java).setAction(Constants.NEXT_ACTION)
        val pnextIntent = PendingIntent.getBroadcast(
            this, 0,
            nextIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val closeIntent =
            Intent(
                this,
                NotificationReceiver::class.java
            ).setAction(Constants.STOPFOREGROUND_ACTION)
        val pcloseIntent = PendingIntent.getBroadcast(
            this, 0,
            closeIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification: Notification = NotificationCompat.Builder(this)
            .setContentTitle(song.title)
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