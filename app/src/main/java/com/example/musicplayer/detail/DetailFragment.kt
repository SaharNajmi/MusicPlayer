package com.example.musicplayer.detail

import android.content.*
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.os.Bundle
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.`interface`.OnSongComplete
import com.example.model.SongModel
import com.example.musicplayer.R
import com.example.musicplayer.databinding.FragmentDetailBinding
import com.example.service.MusicService
import kotlin.random.Random

class DetailFragment : Fragment(), OnSongComplete {
    lateinit var binding: FragmentDetailBinding
    lateinit var musicService: MusicService
    var playIntent: Intent? = null
    var musicBound = false
    lateinit var songModel: SongModel
    val args: DetailFragmentArgs by navArgs()
    var isShuffle = false
    var isRepeat = false
    val listMusic = ArrayList<SongModel>()
    lateinit var player: MediaPlayer
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        songModel = args.musicDetail

        player = MediaPlayer()

        updateUi()

        musicController()
    }

    private val boundServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName?, iBinder: IBinder?) {
            val binder: MusicService.MusicBinder = iBinder as MusicService.MusicBinder
            musicService = binder.getService()

            musicService.getListSong(listMusic)

            //set value UI
            musicService.setUI(
                binding.seekBar,
                binding.seekBar,
                binding.txtStartTime,
                binding.txtEndTime
            )

            //end song
            musicService.songComplete(this@DetailFragment)

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

    fun updateUi() {
        var bitmap: Bitmap? = null
        try {
            bitmap =
                MediaStore.Images.Media.getBitmap(
                    requireContext().contentResolver,
                    songModel.coverImage
                )
            binding.imgCoverSong.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Log.e("Error set image!!", e.toString())
        }
        binding.songTitle.text = songModel.songTitle
        binding.artist.text = songModel.artist
        binding.btnPlayPause.setImageResource(R.drawable.ic_pause)
    }

    fun nextSong() {
        if (isShuffle) {
            var newSong = musicService.songPosition
            while (newSong == musicService.songPosition)
                newSong = Random.nextInt(listMusic.size - 1)
            musicService.songPosition = newSong
            songModel = listMusic[newSong]
            updateUi()
        } else {
            musicService.nextSong()
            songModel = listMusic[musicService.songPosition]
            updateUi()
        }
    }

    fun backSong() {
        if (isShuffle) {
            var newSong = musicService.songPosition
            while (newSong == musicService.songPosition)
                newSong = Random.nextInt(listMusic.size - 1)
            musicService.songPosition = newSong
            songModel = listMusic[newSong]
            updateUi()
        } else {
            musicService.backSong()
            songModel = listMusic[musicService.songPosition]
            updateUi()
        }
    }

    fun playSong(playBtnDetail: ImageView) {
        if (musicService.playerState == 1) {
            playBtnDetail.setImageResource(R.drawable.ic_pause)
            musicService.resumeSong()
        } else if (musicService.playerState == 2) {
            playBtnDetail.setImageResource(R.drawable.ic_play)
            musicService.pauseSong()
        }
    }

    fun musicController() {
        //play and pause song
        val btnPlayPauseDetail = binding.btnPlayPause
        btnPlayPauseDetail.setOnClickListener {
            playSong(btnPlayPauseDetail)
        }

        //next song
        binding.btnNext.setOnClickListener {
            nextSong()
        }

        //previous song
        binding.btnBack.setOnClickListener {
            backSong()
        }
        binding.btnBack.setOnClickListener {
            backSong()
        }
        //shuffle song
        binding.shuffle.setOnClickListener {
            if (isShuffle) {
                isShuffle = false
                binding.shuffle.setImageResource(R.drawable.ic_shuffle)
            } else {
                isShuffle = true
                binding.shuffle.setImageResource(R.drawable.ic_shuffle_pressed)

                isRepeat = false
                binding.repeat.setImageResource(R.drawable.ic_repeat)

            }
        }

        //repeat song
        binding.repeat.setOnClickListener {
            if (isRepeat) {
                isRepeat = false
                binding.repeat.setImageResource(R.drawable.ic_repeat)
            } else {
                isRepeat = true
                binding.repeat.setImageResource(R.drawable.ic_repeat_pressed)

                isShuffle = false
                binding.shuffle.setImageResource(R.drawable.ic_shuffle)
            }
        }
    }

    override fun onSongComplete() {
        when {
            //repeat is on
            isRepeat -> musicService.repeatSong()

            //shuffle is on
            isShuffle -> {
                musicService.shuffleSong()
                songModel = listMusic[musicService.songPosition]
                updateUi()
            }

            //next song
            else -> nextSong()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireContext().stopService(playIntent)
        requireContext().unbindService(boundServiceConnection)
    }
}