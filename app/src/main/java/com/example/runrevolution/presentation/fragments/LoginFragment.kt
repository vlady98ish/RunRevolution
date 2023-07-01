package com.example.runrevolution.presentation.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.runrevolution.R
import com.example.runrevolution.databinding.FragmentLoginBinding
import com.example.runrevolution.utils.other.Constant.KEY_FIRST_TIME
import com.example.runrevolution.utils.other.Constant.KEY_HEIGHT
import com.example.runrevolution.utils.other.Constant.KEY_NAME
import com.example.runrevolution.utils.other.Constant.KEY_WEIGHT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    @Inject
    lateinit var sharedPref: SharedPreferences

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    @set:Inject
    var isFirstTime = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)
        Log.d("pttt", "IN HOME FRAGMENT")
        if(!isFirstTime){
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.loginFragment, true)
                .build()
            findNavController().navigate(
                R.id.action_loginFragment_to_runFragment,
                savedInstanceState,
                navOptions
            )
        }
        setListener()


    }

    private fun setListener() {
        binding.button.setOnClickListener(View.OnClickListener {
            val success = dataToSharePref()
            if(!success){
                findNavController().navigate(R.id.action_loginFragment_to_runFragment)

            } else {
                Snackbar.make(requireView(), "Please fill out all fields", Snackbar.LENGTH_SHORT).show()
            }
        }
        )
    }

    private fun dataToSharePref() : Boolean{
        val name = binding.loginName.text.toString()
        val weight = binding.loginWeight.text.toString()
        val height = binding.loginHeight.text.toString()

        if(name.isEmpty() || weight.isEmpty() || height.isEmpty()){
            return false
        }
        sharedPref.edit()
            .putString(KEY_NAME, name)
            .putFloat(KEY_WEIGHT, weight.toFloat())
            .putFloat(KEY_HEIGHT, height.toFloat())
            .putBoolean(KEY_FIRST_TIME, false)
            .apply()
        return true
    }
}