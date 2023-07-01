package com.example.runrevolution.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runrevolution.R
import com.example.runrevolution.databinding.FragmentHistoryBinding
import com.example.runrevolution.databinding.FragmentLoginBinding
import com.example.runrevolution.presentation.adapter.RunAdapter
import com.example.runrevolution.presentation.viewmodel.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment(R.layout.fragment_history) {



    private val historyViewModel: HistoryViewModel by viewModels()

    private lateinit var runAdapter: RunAdapter

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHistoryBinding.bind(view)
        setUpRecyclerView()
        Log.d("pttt", "IN HISTORY FRAGMENT")

        historyViewModel.runningDetails.observe(viewLifecycleOwner, Observer {
            runAdapter.submitList(it)
        })


    }

    private fun setUpRecyclerView()  = binding.rvHistory.apply {
        runAdapter = RunAdapter()
        adapter = runAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }


}