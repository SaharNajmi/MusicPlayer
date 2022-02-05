package com.example.musicplayer.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.model.SongModel
import com.example.musicplayer.R
import com.example.musicplayer.databinding.FragmentDetailBinding
import com.example.musicplayer.main.MainActivity
import com.example.musicplayer.main.ViewModelFactory
import com.example.musicplayer.player.PlayerState
import java.util.concurrent.TimeUnit


class DetailFragment : Fragment() {
    lateinit var binding: FragmentDetailBinding
    lateinit var songModel: SongModel
    lateinit var viewModel: DetailViewModel

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
        val args: DetailFragmentArgs by navArgs()
        songModel = args.musicDetail

        //viewModel
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory()
        ).get(DetailViewModel::class.java)

        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.hideMusicController()
        }

        updateUi(songModel)

        musicController()

        //update ui music detail controller
        viewModel.songModel.observe(requireActivity(), {
            songModel = it
            updateUi(it)
        })

        //update ui button pause or play music
        viewModel.playerState.observe(requireActivity(), {
            updateUiPlayOrPause(it)
        })

        // Seek bar change listener
        seekBarChangeListener(binding.seekBar)

        //update progressbar
        viewModel.progress.observe(requireActivity(), { progress ->
            binding.seekBar.progress = progress
        })
    }

    fun seekBarChangeListener(seekBar: SeekBar) {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p2)
                    viewModel.playerSeekTo(p1)

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
        binding.seekBar.max = viewModel.getNewSongDuration()
        binding.txtEndTime.text = durationPointSeekBar(viewModel.getNewSongDuration())
        if (activity != null) {
            Glide.with(this)
                .load(songModel.coverImage)
                .into(binding.imgCoverSong)
        }

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
        if (viewModel.getShuffle())
            binding.shuffle.setImageResource(R.drawable.ic_shuffle_pressed)
        if (viewModel.getRepeat())
            binding.repeat.setImageResource(R.drawable.ic_repeat_pressed)
    }

    fun musicController() {
        //play and pause song
        val btnPlayPauseDetail = binding.btnPlayPause
        btnPlayPauseDetail.setOnClickListener {
            viewModel.toggleState()
        }

        //next song
        binding.btnNext.setOnClickListener {
            viewModel.nextSong()
        }

        //previous song
        binding.btnBack.setOnClickListener {
            viewModel.backSong()
        }

        //shuffle song
        binding.shuffle.setOnClickListener {
            if (viewModel.changeIsShuffle()) {
                binding.shuffle.setImageResource(R.drawable.ic_shuffle_pressed)
                binding.repeat.setImageResource(R.drawable.ic_repeat)
            } else
                binding.shuffle.setImageResource(R.drawable.ic_shuffle)
        }

        //repeat song
        binding.repeat.setOnClickListener {
            if (viewModel.changeIsRepeat()) {
                binding.repeat.setImageResource(R.drawable.ic_repeat_pressed)
                binding.shuffle.setImageResource(R.drawable.ic_shuffle)
            } else
                binding.repeat.setImageResource(R.drawable.ic_repeat)
        }

        //search lyrics
        binding.btnSearchLyrics.setOnClickListener {
            findNavController().navigate(
                DetailFragmentDirections.actionDetailFragmentToSearchLyricsFragment(
                    viewModel.songModel.value!!
                )
            )
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