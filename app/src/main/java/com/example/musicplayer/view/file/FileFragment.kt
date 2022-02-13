package com.example.musicplayer.view.file

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.databinding.FragmentFileBinding
import com.example.musicplayer.factory.BaseViewModelFactory
import com.example.musicplayer.view.main.MainFragmentDirections

class FileFragment : Fragment(), FileAdapter.FileEventListener {
    lateinit var binding: FragmentFileBinding
    lateinit var viewModel: FileViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //viewModel
        viewModel = ViewModelProvider(
            requireActivity(),
            BaseViewModelFactory(requireContext())
        ).get(FileViewModel::class.java)

        //show list folders
        showFolders()
    }

    private fun showFolders() {
        binding.recyclerFile.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerFile.adapter = FileAdapter(viewModel.getFiles(requireContext()), this)
    }

    override fun onFileItemClick(fileName: String) {
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToFileDetailFragment(
                fileName
            )
        )
    }
}