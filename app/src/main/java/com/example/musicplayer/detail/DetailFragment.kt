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
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.model.SongModel
import com.example.musicplayer.R
import com.example.musicplayer.databinding.FragmentDetailBinding
import com.example.musicplayer.main.MainActivity
import com.example.musicplayer.player.Player

class DetailFragment : Fragment() {
    lateinit var binding: FragmentDetailBinding
    lateinit var songModel: SongModel
    val args: DetailFragmentArgs by navArgs()
    var listMusic = ArrayList<SongModel>()
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
    }

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