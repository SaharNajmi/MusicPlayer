package com.example.musicplayer.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.model.SongModel
import com.example.musicplayer.R
import com.example.musicplayer.databinding.FragmentDetailBinding
import com.example.musicplayer.main.MainActivity
import com.example.musicplayer.player.Player
import com.example.musicplayer.player.PlayerState
import java.util.concurrent.TimeUnit

class DetailFragment : Fragment() {
    lateinit var binding: FragmentDetailBinding
    lateinit var songModel: SongModel
    val args: DetailFragmentArgs by navArgs()
    lateinit var myPlayer: Player

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
        myPlayer = Player.getInstance()

        //Call hideMusicController function when user wants to DetailFragment
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.hideMusicController()
        }

        updateUi(songModel)

        musicController()

        //update ui music detail controller
        myPlayer.songModel.observe(requireActivity(), {
            songModel = it
            updateUi(it)
        })

        //update ui button pause or play music
        myPlayer.playerState.observe(requireActivity(), {
            updateUiPlayOrPause(it)
        })

        //// Seek bar change listener
        seekBarChangeListener(binding.seekBar)

        //update progressbar
        myPlayer.progress.observe(requireActivity(), {
            updateProgress(it)
        })
    }

    fun updateProgress(progress: Int) {
        //set progress to seekbar
        binding.seekBar.max = myPlayer.duration
        binding.seekBar.progress = progress
    }

    fun seekBarChangeListener(seekBar: SeekBar) {
        binding.txtEndTime.text = durationPointSeekBar(myPlayer.duration)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p2)
                    myPlayer.playerSeekTo(p1)

                binding.txtStartTime.text = durationPointSeekBar(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }

    fun durationPointSeekBar(duration: Int): String = String.format(
        "%d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(duration.toLong()),
        TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration.toLong()))
    )

    fun updateUi(songModel: SongModel) {
        binding.songTitle.text = songModel.songTitle
        binding.artist.text = songModel.artist
        Glide.with(this)
            .load(songModel.coverImage)
            .into(binding.imgCoverSong)

        updateUiShuffleOrRepeat()
    }

    fun updateUiPlayOrPause(playerState: PlayerState) {
        if (playerState == PlayerState.PAUSED) {
            binding.btnPlayPause.setImageResource(R.drawable.ic_play)
        } else if (playerState == PlayerState.PLAYING) {
            binding.btnPlayPause.setImageResource(R.drawable.ic_pause)
        }
    }

    fun updateUiShuffleOrRepeat() {
        if (myPlayer.isShuffle)
            binding.shuffle.setImageResource(R.drawable.ic_shuffle_pressed)
        if (myPlayer.isRepeat)
            binding.repeat.setImageResource(R.drawable.ic_repeat_pressed)
    }

    fun musicController() {
        //play and pause song
        val btnPlayPauseDetail = binding.btnPlayPause
        btnPlayPauseDetail.setOnClickListener {
            myPlayer.toggleState()
        }

        //next song
        binding.btnNext.setOnClickListener {
            myPlayer.nextSong()
        }

        //previous song
        binding.btnBack.setOnClickListener {
            myPlayer.backSong()
        }

        //shuffle song
        binding.shuffle.setOnClickListener {
            if (myPlayer.isShuffle) {
                myPlayer.isShuffle = false
                binding.shuffle.setImageResource(R.drawable.ic_shuffle)
            } else {
                myPlayer.isShuffle = true
                binding.shuffle.setImageResource(R.drawable.ic_shuffle_pressed)

                myPlayer.isRepeat = false
                binding.repeat.setImageResource(R.drawable.ic_repeat)
            }
        }

        //repeat song
        binding.repeat.setOnClickListener {
            if (myPlayer.isRepeat) {
                myPlayer.isRepeat = false
                binding.repeat.setImageResource(R.drawable.ic_repeat)
            } else {
                myPlayer.isRepeat = true
                binding.repeat.setImageResource(R.drawable.ic_repeat_pressed)

                myPlayer.isShuffle = false
                binding.shuffle.setImageResource(R.drawable.ic_shuffle)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        //Call showMusicController function when user go back
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.showMusicController()
        }
    }
}