package com.example.runrevolution.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.runrevolution.R
import com.example.runrevolution.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    private lateinit var binding : ActivityStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("pttt", "IN START ACTIVITY")

        binding.bottomNavigationView.setupWithNavController(findNavController(R.id.sosNavHostFragment))
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.flFragment) as NavHostFragment
//        val sosNavHostFragment = navHostFragment.childFragmentManager.findFragmentById(R.id.sosNavHostFragment) as NavHostFragment
//        sosNavHostFragment.findNavController().addOnDestinationChangedListener{ _, destination, _ ->
//            when(destination.id) {
//                R.id.settingsFragment, R.id.runFragment, R.id.historyFragment ->
//                    binding.bottomNavigationView.visibility = View.VISIBLE
//                else -> binding.bottomNavigationView.visibility = View.GONE
//            }
//        }
    }


}