package com.example.runrevolution.presentation.activity


import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.util.Log
import android.view.View


import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.runrevolution.R
import com.example.runrevolution.presentation.service.RunningService.Companion.ACTION_SHOW_RUNNING_FRAGMENT
import com.example.runrevolution.databinding.ActivityStartBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("pttt", "IN START ACTIVITY")
        navigateToRunningFragmentAfterAction(intent)



        binding.bottomNavigationView.setupWithNavController(findNavController(R.id.sosNavHostFragment))


        findNavController(R.id.sosNavHostFragment).addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.settingsFragment, R.id.runFragment, R.id.historyFragment, R.id.mapFragment ->
                    binding.bottomNavigationView.visibility = View.VISIBLE

                else -> binding.bottomNavigationView.visibility = View.GONE
            }
        }
    }


    private fun navigateToRunningFragmentAfterAction(intent: Intent?) {
        if (intent?.action == ACTION_SHOW_RUNNING_FRAGMENT) {
            findNavController(R.id.sosNavHostFragment).navigate(R.id.action_global_runningFragment)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToRunningFragmentAfterAction(intent)
    }


}