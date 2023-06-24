package com.example.musicplayer.view.file

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.databinding.FragmentFileBinding
import com.example.musicplayer.view.main.MainFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FileFragment : Fragment(), FileAdapter.FileEventListener {
    lateinit var binding: FragmentFileBinding
    private val viewModel: FileViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showFolders()
    }

    private fun showFolders() {
        binding.recyclerFile.layoutManager = LinearLayoutManager(requireContext())
        viewModel.fileNames.observe(viewLifecycleOwner, { list ->
            binding.recyclerFile.adapter = FileAdapter(list, this)
        })
    }

    override fun onFileItemClick(fileName: String) {
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToFileDetailFragment(
                fileName
            )
        )
    }
}