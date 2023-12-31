package com.example.musicplayer.view.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.musicplayer.R
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.databinding.FragmentDetailBinding
import com.example.musicplayer.utils.PlayerState
import com.example.musicplayer.view.favorite.FavoriteViewModel
import com.example.musicplayer.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class DetailFragment : Fragment() {
    lateinit var binding: FragmentDetailBinding
    lateinit var song: Song
    private val viewModel: DetailViewModel by viewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    var favorite: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: DetailFragmentArgs by navArgs()
        song = args.musicDetail

        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.hideMusicController()
        }

        updateUi(song)

        musicController()

        viewModel.song.observe(requireActivity(), {
            song = it
            updateUi(it)
        })

        updateUiPlayOrPause(PlayerState.PAUSED)
        viewModel.playerState.observe(requireActivity(), {
            updateUiPlayOrPause(it)
        })

        showOldProgressSeekBar()

        seekBarChangeListener(binding.seekBar)

        viewModel.progress.observe(requireActivity(), { progress ->
            binding.seekBar.progress = progress
        })

        if (song.isLyrics)
            binding.txtLyrics.text = song.lyrics
        else
            binding.txtLyrics.visibility = View.GONE

        favorite = song.favorite
        changeFavorite()

        binding.btnBackPage.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    fun checkFavorite(isFavorite: Boolean) {
        if (isFavorite)
            binding.favorite.setImageResource(R.drawable.ic_favorite)
        else
            binding.favorite.setImageResource(R.drawable.ic_not_favorite)
    }

    fun changeFavorite() {
        binding.favorite.setOnClickListener {
            favorite = if (favorite) {
                binding.favorite.setImageResource(R.drawable.ic_not_favorite)
                favoriteViewModel.delete(song)
                false
            } else {
                binding.favorite.setImageResource(R.drawable.ic_favorite)
                favoriteViewModel.insert(song)
                true
            }
        }
    }

    private fun showOldProgressSeekBar() {
        val oldProgressSong = viewModel.getSharedPreference("progress", 0)
        updateProgress(oldProgressSong, song.duration)
    }

    fun updateProgress(progress: Int, duration: Int) {
        binding.seekBar.max = duration
        binding.seekBar.progress = progress
        binding.txtStartTime.text = durationPointSeekBar(progress)
        binding.txtEndTime.text = durationPointSeekBar(duration)
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

    fun updateUi(song: Song) {
        binding.songTitle.text = song.title
        binding.artist.text = song.artist
        binding.seekBar.max = song.duration
        binding.txtEndTime.text = durationPointSeekBar(song.duration)
        if (activity != null) {
            Glide.with(this)
                .load(song.coverImage)
                .into(binding.imgCoverSong)
        }

        updateUiShuffleOrRepeat()
        checkFavorite(song.favorite)
    }

    fun updateUiPlayOrPause(playerState: PlayerState) {
        if (playerState == PlayerState.PAUSED) {
            binding.btnPlayPause.setImageResource(R.drawable.ic_play)
        } else if (playerState == PlayerState.PLAYING) {
            binding.btnPlayPause.setImageResource(R.drawable.ic_pause)
        }
    }

    fun updateUiShuffleOrRepeat() {
        if (viewModel.shuffle)
            binding.shuffle.setImageResource(R.drawable.ic_shuffle_pressed)
        if (viewModel.repeat)
            binding.repeat.setImageResource(R.drawable.ic_repeat_pressed)
    }

    fun musicController() {
        binding.btnPlayPause.setOnClickListener {
            viewModel.toggleState()
        }

        binding.btnNext.setOnClickListener {
            viewModel.nextSong()
        }

        binding.btnBack.setOnClickListener {
            viewModel.backSong()
        }

        binding.shuffle.setOnClickListener {
            if (viewModel.changeIsShuffle()) {
                binding.shuffle.setImageResource(R.drawable.ic_shuffle_pressed)
                binding.repeat.setImageResource(R.drawable.ic_repeat)
            } else
                binding.shuffle.setImageResource(R.drawable.ic_shuffle)
        }

        binding.repeat.setOnClickListener {
            if (viewModel.changeIsRepeat()) {
                binding.repeat.setImageResource(R.drawable.ic_repeat_pressed)
                binding.shuffle.setImageResource(R.drawable.ic_shuffle)
            } else
                binding.repeat.setImageResource(R.drawable.ic_repeat)
        }

        binding.btnSearchLyrics.setOnClickListener {
            findNavController().navigate(
                DetailFragmentDirections.actionDetailFragmentToSearchLyricsFragment(
                    viewModel.song.value!!
                )
            )
        }
    }

    override fun onStop() {
        super.onStop()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.showMusicController()
        }
    }
}