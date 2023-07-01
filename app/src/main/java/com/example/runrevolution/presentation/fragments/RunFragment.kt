package com.example.runrevolution.presentation.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

import android.os.Bundle
import android.provider.CalendarContract
import android.provider.Settings
import android.util.Log

import android.view.View

import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.runrevolution.R
import com.example.runrevolution.databinding.FragmentRunBinding
import com.example.runrevolution.domain.model.LocationDetails
import com.example.runrevolution.domain.model.RunDetails
import com.example.runrevolution.presentation.service.RunningService
import com.example.runrevolution.presentation.service.RunningService.Companion.ACTION_START_SERVICE
import com.example.runrevolution.presentation.service.RunningService.Companion.ACTION_STOP_SERVICE
import com.example.runrevolution.presentation.viewmodel.RunViewModel
import com.example.runrevolution.utils.other.Constant.LINE_COLOR
import com.example.runrevolution.utils.other.Constant.LINE_WIDTH
import com.example.runrevolution.utils.other.TimeUtility
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions

import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import javax.inject.Inject


@AndroidEntryPoint
class RunFragment : Fragment(R.layout.fragment_run) {

    private var _binding: FragmentRunBinding? = null
    private val binding get() = _binding!!
    private var map: GoogleMap? = null
    private val runViewModel: RunViewModel by viewModels()
    private var isServiceRunning = false
    private var polyline: Polyline? = null

    @set:Inject
     var weight = 70f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRunBinding.bind(view)




        binding.runMAPView.onCreate(savedInstanceState)

        binding.runMAPView.getMapAsync {
            map = it
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                map?.isMyLocationEnabled = true
            }
        }
        setListener()

        lifecycleScope.launchWhenStarted {
            runViewModel.runningState.collect { isRunning ->
                isServiceRunning = isRunning
                if (isServiceRunning) {
                    startAnimation()
                } else {
                    stopAnimation()
                }
            }
        }

        runViewModel.locationPoints.observe(viewLifecycleOwner, Observer { locationPoints ->
            if (locationPoints.isNotEmpty()) {
                val lastLocation = locationPoints.last()
                Log.d(
                    "pttt",
                    "Foreground location: ${lastLocation.latitude} ${lastLocation.longitude}"
                )
                updatePolyline(locationPoints)
            }
        })

        runViewModel.timeInSeconds.observe(viewLifecycleOwner, Observer {
            binding.runTVTimer.text = TimeUtility.getFormattedTime(it * 1000L)
        })

        runViewModel.distance.observe(viewLifecycleOwner, Observer {
            val distance = String.format("%.1f", it)
            binding.runTVDistance.text = distance
        })

        runViewModel.speed.observe(viewLifecycleOwner, Observer {
            val speed = String.format("%.1f", it)
            binding.runTVCalories.text = speed
        })

    }


    private fun updatePolyline(locationPoints: List<LocationDetails>) {
        val latLngList = locationPoints.map { LatLng(it.latitude, it.longitude) }
        Log.d("pttt", latLngList.size.toString())
        if (polyline == null) {
            // Create a new polyline
            polyline = map?.addPolyline(
                PolylineOptions().addAll(latLngList).color(LINE_COLOR).width(
                    LINE_WIDTH
                )
            )
        } else {
            // Update existing polyline
            polyline?.points = latLngList
        }

        // Move camera to the last location
        val lastLocation = latLngList.lastOrNull()
        if (lastLocation != null) {
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLocation, 15f))
        }
    }

    private fun zoomForSnapshot(){
        val builder = LatLngBounds.Builder()
        for (marker in runViewModel.locationPoints.value!!) {
            builder.include(LatLng(marker.latitude, marker.longitude))
        }
        val bounds = builder.build()
        val padding = 100 // offset from edges of the map in pixels
        val cu = CameraUpdateFactory
            .newLatLngBounds(
                bounds,
                binding.runMAPView.width,
                binding.runMAPView.height,
                (binding.runMAPView.height * 0.05f).toInt())
        map?.moveCamera(cu)
    }

    private fun endAndSaveRun(){
        map?.snapshot { bmp ->
            val date = Calendar.getInstance().timeInMillis
            val avgSpeed = runViewModel.speed.value!!
            val distance = runViewModel.distance.value!!
            val time = runViewModel.timeInSeconds.value!!
            val run = RunDetails(bmp, date, avgSpeed, distance, time)
            runViewModel.saveRunDetails(run)
            clear()
        }
    }


    @SuppressLint("MissingPermission")
    private fun setListener() {
        binding.circularButton.setOnClickListener(View.OnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                onLocationPermissionGranted()
                map?.isMyLocationEnabled = true
                if (isServiceRunning) {
                    sendActionToService(ACTION_STOP_SERVICE)
                    endAndSaveRun()
                } else {
                    sendActionToService(ACTION_START_SERVICE)
                }

            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_LOCATION_CODE
                )
            }


        })
    }

    private fun clearMap() {
        runViewModel.deleteAllLocations()
        runViewModel.clearPoints()
        updatePolyline(listOf())
    }

    private fun clearTimer() {
        binding.runTVTimer.text = TimeUtility.getFormattedTime(0 * 1000L)

    }

    private fun clearDistance() {
        binding.runTVDistance.text = "0.0"

    }

    private fun clearCalories() {
        binding.runTVCalories.text = "0.0"

    }

    private fun clear() {
        clearMap()
        clearTimer()
        clearDistance()
        clearCalories()
    }

    private fun startAnimation() {
        val pulseAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.pulse)
        binding.circularButton.startAnimation(pulseAnimation)
        binding.circularButton.setIconResource(R.drawable.ic_stop)
        binding.circularButton.setBackgroundResource(R.drawable.rounded_button_stop)

        isServiceRunning = true
    }

    private fun stopAnimation() {
        isServiceRunning = false
        binding.circularButton.setIconResource(R.drawable.ic_play)
        binding.circularButton.setBackgroundResource(R.drawable.rounded_button)
        binding.circularButton.clearAnimation()
    }

    override fun onResume() {
        super.onResume()
        binding.runMAPView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.runMAPView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.runMAPView.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.runMAPView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.runMAPView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        runViewModel.deleteAllLocations()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.runMAPView.onSaveInstanceState(outState)
    }

    private fun sendActionToService(action: String) {
        Intent(requireContext(), RunningService::class.java).also {
            it.action = action
            requireContext().startForegroundService(it)
        }


    }


    private fun askUserForOpeningAppSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireContext().packageName, null)
        )
        if (requireContext().packageManager.resolveActivity(
                appSettingsIntent,
                PackageManager.MATCH_DEFAULT_ONLY
            ) == null
        ) {
            Toast.makeText(requireContext(), "Permission Denied Forever", Toast.LENGTH_SHORT).show()
        } else {
            AlertDialog.Builder(requireContext())
                .setTitle("Permission Denied")
                .setMessage(
                    "Permission Denied Forever. \n\n" +
                            "You can change them in ap settings \n\n" +
                            "Would you like to open app settings?"
                )
                .setPositiveButton("Open") { _, _ ->
                    startActivity(appSettingsIntent)
                }
                .create()
                .show()
        }
    }

    private fun onLocationPermissionGranted() {
        Toast.makeText(requireContext(), "Location granted", Toast.LENGTH_SHORT).show()
    }


    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_LOCATION_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onLocationPermissionGranted()
                } else {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        askUserForOpeningAppSettings()
                    } else {
                        Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private companion object {
        const val PERMISSION_LOCATION_CODE = 10001
    }

}