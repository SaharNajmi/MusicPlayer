package com.example.musicplayer.all

import android.content.*
import android.graphics.Bitmap
import android.os.Bundle
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.`interface`.OnSongComplete
import com.example.`interface`.OnSongPosition
import com.example.`interface`.SongEventListener
import com.example.adapter.SongAdapter
import com.example.model.SongModel
import com.example.musicplayer.R
import com.example.musicplayer.ReadExternalDate
import com.example.musicplayer.databinding.FragmentHomeBinding
import com.example.musicplayer.main.MainFragmentDirections
import com.example.service.MusicService
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlin.random.Random

class HomeFragment : Fragment(), SongEventListener, OnSongPosition,
    OnSongComplete {
    lateinit var binding: FragmentHomeBinding
    lateinit var musicService: MusicService
    var playIntent: Intent? = null
    var musicBound = false
    lateinit var songModel: SongModel
    var isShuffle = false
    var isRepeat = false
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    var listMusic = ArrayList<SongModel>()

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
        //get all songs from phone
        listMusic = ReadExternalDate().readExternalData(requireContext())

        //show list items into recyclerView
        showListMusic()

        //buttons control song
        musicController()

        //detail music (with BottomSheetBehavior)
        bottomSheetDetailMusic()
    }

    private val boundServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName?, iBinder: IBinder?) {
            val binder: MusicService.MusicBinder = iBinder as MusicService.MusicBinder
            musicService = binder.getService()

            musicService.getListSong(listMusic)

            //set value UI
            musicService.setUI(
                binding.playMusicLayout.seekBar,
                binding.detailMusicLayout.seekBar,
                binding.detailMusicLayout.txtStartTime,
                binding.detailMusicLayout.txtEndTime
            )

            //end song
            musicService.songComplete(this@HomeFragment)

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

    fun bottomSheetDetailMusic() {
        //open or expended
        bottomSheetBehavior = BottomSheetBehavior.from(binding.detailMusicLayout.bottomSheet)
        binding.playMusicLayout.layoutController.setOnClickListener {
            /* if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                 bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
             else
                 bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED*/

            val directions = MainFragmentDirections.actionMainFragmentToDetailFragment(songModel)
            findNavController().navigate(directions)

        }
        //close
        binding.detailMusicLayout.btnDown.setOnClickListener {
            //   bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    fun showListMusic() {
        binding.recyclerMusics.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMusics.adapter = SongAdapter(requireContext(), listMusic, this, this)
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
            binding.detailMusicLayout.imgCoverSong.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Log.e("Error set image!!", e.toString())
        }
        binding.playMusicLayout.songTitle.text = songModel.songTitle
        binding.playMusicLayout.artist.text = songModel.artist
        binding.detailMusicLayout.songTitle.text = songModel.songTitle
        binding.detailMusicLayout.artist.text = songModel.artist
        binding.detailMusicLayout.btnPlayPause.setImageResource(R.drawable.ic_pause)
        binding.playMusicLayout.btnPlayPause.setImageResource(R.drawable.ic_pause)
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

    fun playSong(playBtn: ImageView, playBtnDetail: ImageView) {
        if (musicService.playerState == 1) {
            playBtn.setImageResource(R.drawable.ic_pause)
            playBtnDetail.setImageResource(R.drawable.ic_pause)
            musicService.resumeSong()
        } else if (musicService.playerState == 2) {
            playBtn.setImageResource(R.drawable.ic_play)
            playBtnDetail.setImageResource(R.drawable.ic_play)
            musicService.pauseSong()
        }
    }

    fun musicController() {
        //play and pause song
        val btnPlayPause = binding.playMusicLayout.btnPlayPause
        val btnPlayPauseDetail = binding.detailMusicLayout.btnPlayPause
        btnPlayPause.setOnClickListener {
            playSong(btnPlayPause, btnPlayPauseDetail)
        }

        btnPlayPauseDetail.setOnClickListener {
            playSong(btnPlayPause, btnPlayPauseDetail)
        }

        //next song
        binding.playMusicLayout.btnNext.setOnClickListener {
            nextSong()
        }
        binding.detailMusicLayout.btnNext.setOnClickListener {
            nextSong()
        }

        //previous song
        binding.playMusicLayout.btnBack.setOnClickListener {
            backSong()
        }
        binding.detailMusicLayout.btnBack.setOnClickListener {
            backSong()
        }
        //shuffle song
        binding.detailMusicLayout.shuffle.setOnClickListener {
            if (isShuffle) {
                isShuffle = false
                binding.detailMusicLayout.shuffle.setImageResource(R.drawable.ic_shuffle)
            } else {
                isShuffle = true
                binding.detailMusicLayout.shuffle.setImageResource(R.drawable.ic_shuffle_pressed)

                isRepeat = false
                binding.detailMusicLayout.repeat.setImageResource(R.drawable.ic_repeat)

            }
        }

        //repeat song
        binding.detailMusicLayout.repeat.setOnClickListener {
            if (isRepeat) {
                isRepeat = false
                binding.detailMusicLayout.repeat.setImageResource(R.drawable.ic_repeat)
            } else {
                isRepeat = true
                binding.detailMusicLayout.repeat.setImageResource(R.drawable.ic_repeat_pressed)

                isShuffle = false
                binding.detailMusicLayout.shuffle.setImageResource(R.drawable.ic_shuffle)
            }
        }
    }

    override fun onSelect(songModel: SongModel) {
        binding.playMusicLayout.layoutController.visibility = View.VISIBLE
        this.songModel = songModel
        musicService.setSong(songModel)
        updateUi()
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

    override fun onSongPosition(pos: Int) {
        musicService.setPositionSong(pos)
    }

    override fun onDestroy() {
        super.onDestroy()
        requireContext().stopService(playIntent)
        requireContext().unbindService(boundServiceConnection)
    }
}