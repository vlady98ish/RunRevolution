package com.example.runrevolution.presentation.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.runrevolution.R
import com.example.runrevolution.databinding.FragmentLoginBinding
import com.example.runrevolution.databinding.FragmentSettingsBinding
import com.example.runrevolution.utils.other.Constant.CHANGES_APPLIED
import com.example.runrevolution.utils.other.Constant.KEY_NAME
import com.example.runrevolution.utils.other.Constant.KEY_WEIGHT
import com.example.runrevolution.utils.other.Constant.PLEASE_FILL_OUT_ALL_FIELDS
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject



@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private fun applyChanges(): Boolean {
        val name = binding.settingsLoginName.text.toString()
        val weight = binding.settingsLoginWeight.text.toString()
        if (name.isEmpty() || weight.isEmpty()) {
            return false
        }
        sharedPreferences.edit()
            .putString(KEY_NAME, name)
            .putFloat(KEY_WEIGHT, weight.toFloat())
            .apply()
        return true
    }

    private fun loadDataFromSharedPreferences() {
        val name = sharedPreferences.getString(KEY_NAME, "")
        val weight = sharedPreferences.getFloat(KEY_WEIGHT, 80f)
        binding.settingsLoginName.setText(name)
        binding.settingsLoginWeight.setText(weight.toString())
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)
        loadDataFromSharedPreferences()
        binding.settingsButton.setOnClickListener(View.OnClickListener {
            val success = applyChanges()
            if (success) {
                Snackbar.make(requireView(), CHANGES_APPLIED, Snackbar.LENGTH_SHORT)
                    .show()

            } else {
                Snackbar.make(requireView(), PLEASE_FILL_OUT_ALL_FIELDS, Snackbar.LENGTH_SHORT)
                    .show()
            }
        })
    }
}