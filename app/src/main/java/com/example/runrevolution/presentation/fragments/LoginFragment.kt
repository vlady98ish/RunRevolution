package com.example.runrevolution.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.runrevolution.R
import com.example.runrevolution.databinding.FragmentLoginBinding


class LoginFragment : Fragment(R.layout.fragment_login) {



    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)
        Log.d("pttt", "IN HOME FRAGMENT")
        setListener()


    }

    private fun setListener() {
        binding.button.setOnClickListener(View.OnClickListener {

        }
        )
    }
}