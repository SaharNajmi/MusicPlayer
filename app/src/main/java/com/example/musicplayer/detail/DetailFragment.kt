package com.example.musicplayer.detail

import android.graphics.Bitmap
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.model.SongModel
import com.example.musicplayer.R
import com.example.musicplayer.databinding.FragmentDetailBinding
import com.example.musicplayer.main.MainActivity
import com.example.musicplayer.player.Player
import java.util.concurrent.TimeUnit

class DetailFragment : Fragment() {
    lateinit var binding: FragmentDetailBinding
    lateinit var songModel: SongModel
    val args: DetailFragmentArgs by navArgs()
    var listMusic = ArrayList<SongModel>()

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

        listMusic = Player.getListSong(requireContext())

        updateUi(songModel)

        //update ui music detail controller
        Player.liveDataSongModel.observe(requireActivity(), {
            songModel = it
            updateUi(it)
        })

        musicController()

        //Call hideMusicController function when user wants to DetailFragment
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.hideMusicController()
        }

        //seekBar
        setProgressSeekBar(binding.seekBar)
    }

    fun updateProgress(duration: Int) {
        binding.seekBar.max = duration
        val progressRunner = progressRunner(Player.player)
        binding.seekBar.postDelayed(progressRunner, 1000)
    }

    fun progressRunner(mp: MediaPlayer): Runnable {
        val progressRunner: Runnable = object : Runnable {
            override fun run() {
                binding.seekBar.progress = mp.currentPosition
                if (mp.isPlaying) {
                    binding.seekBar.postDelayed(this, 1000)
                }
            }
        }
        return progressRunner
    }

    fun setProgressSeekBar(seekBar: SeekBar) {
        binding.txtEndTime.text = durationPointSeekBar(Player.player.duration)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p2)
                    Player.player.seekTo(p1)

                binding.txtStartTime.text = durationPointSeekBar(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        updateProgress(Player.player.duration)
    }

    fun durationPointSeekBar(duration: Int): String = String.format(
        "%d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(duration.toLong()),
        TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration.toLong()))
    )

    override fun onStop() {
        super.onStop()
        //Call showMusicController function when user go back
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.showMusicController()
        }
    }

    fun updateUi(songModel: SongModel) {
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

        if (Player.liveDataPlayerState.value == 1) {
            binding.btnPlayPause.setImageResource(R.drawable.ic_play)
        } else if (Player.liveDataPlayerState.value == 2) {
            binding.btnPlayPause.setImageResource(R.drawable.ic_pause)
        }
        updateUiShuffleOrRepeat()
    }

    fun updateUiShuffleOrRepeat() {
        if (Player.isShuffle)
            binding.shuffle.setImageResource(R.drawable.ic_shuffle_pressed)
        if (Player.isRepeat)
            binding.repeat.setImageResource(R.drawable.ic_repeat_pressed)
    }

    fun playSong(playBtnDetail: ImageView) {
        if (Player.liveDataPlayerState.value == 1) {
            playBtnDetail.setImageResource(R.drawable.ic_pause)
            Player.resumeSong()
        } else if (Player.liveDataPlayerState.value == 2) {
            playBtnDetail.setImageResource(R.drawable.ic_play)
            Player.pauseSong()
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
            Player.nextSong(requireContext())
        }

        //previous song
        binding.btnBack.setOnClickListener {
            Player.backSong(requireContext())
        }

        //shuffle song
        binding.shuffle.setOnClickListener {
            if (Player.isShuffle) {
                Player.isShuffle = false
                binding.shuffle.setImageResource(R.drawable.ic_shuffle)
            } else {
                Player.isShuffle = true
                binding.shuffle.setImageResource(R.drawable.ic_shuffle_pressed)

                Player.isRepeat = false
                binding.repeat.setImageResource(R.drawable.ic_repeat)
            }
        }

        //repeat song
        binding.repeat.setOnClickListener {
            if (Player.isRepeat) {
                Player.isRepeat = false
                binding.repeat.setImageResource(R.drawable.ic_repeat)
            } else {
                Player.isRepeat = true
                binding.repeat.setImageResource(R.drawable.ic_repeat_pressed)

                Player.isShuffle = false
                binding.shuffle.setImageResource(R.drawable.ic_shuffle)
            }
        }
    }
}