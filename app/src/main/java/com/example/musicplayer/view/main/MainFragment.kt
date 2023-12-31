package com.example.musicplayer.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.musicplayer.R
import com.example.musicplayer.databinding.FragmentMainBinding
import com.example.musicplayer.view.album.AlbumFragment
import com.example.musicplayer.view.all.HomeFragment
import com.example.musicplayer.view.artist.ArtistFragment
import com.example.musicplayer.view.file.FileFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment() {
    lateinit var binding: FragmentMainBinding
    val tabsName = arrayListOf("All", "Folders", "Albums", "Artists")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.inflateMenu(R.menu.main_menu)

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_favorites -> {
                    findNavController().navigate(R.id.action_mainFragment_to_favoriteFragment)
                    true
                }
                R.id.action_search -> {
                    findNavController().navigate(R.id.action_mainFragment_to_searchMusicFragment)
                    true
                }
                else -> false
            }
        }

        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager
        viewPager.adapter = FragmentAdapter(requireContext() as AppCompatActivity)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabsName[position]
        }.attach()
    }

    class FragmentAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 4

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> HomeFragment()
                1 -> FileFragment()
                2 -> AlbumFragment()
                else -> ArtistFragment()
            }
        }
    }
}