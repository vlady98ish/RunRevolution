package com.example.runrevolution.presentation.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.runrevolution.R
import com.example.runrevolution.databinding.FragmentMapBinding
import com.example.runrevolution.domain.model.LocationDetails
import com.example.runrevolution.presentation.viewmodel.MapViewModel
import com.example.runrevolution.utils.other.Constant
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map) {

    private var _binding: FragmentMapBinding? = null
    private val mapViewModel: MapViewModel by viewModels()

    private val binding get() = _binding!!
    private var map: GoogleMap? = null
    private var polyline: Polyline? = null
    private var isServiceRunning = false

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMapBinding.bind(view)
        binding.mapMAPFullView.onCreate(savedInstanceState)
        binding.mapMAPFullView.getMapAsync {
            map = it
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                map?.isMyLocationEnabled = true
            }
        }

        mapViewModel.locationPoints.observe(viewLifecycleOwner, Observer { locationPoints ->
            if (locationPoints.isNotEmpty()) {
                val lastLocation = locationPoints.last()
                Log.d(
                    "pttt",
                    "Foreground location: ${lastLocation.latitude} ${lastLocation.longitude}"
                )
                updatePolyline(locationPoints)
            }
        })

        lifecycleScope.launchWhenStarted {
            mapViewModel.runningState.collect { isRunning ->
                isServiceRunning = isRunning
            }
        }


    }

    private fun updatePolyline(locationPoints: List<LocationDetails>) {
        val latLngList = locationPoints.map { LatLng(it.latitude, it.longitude) }
        Log.d("pttt", latLngList.size.toString())
        if (polyline == null) {
            // Create a new polyline
            polyline = map?.addPolyline(
                PolylineOptions().addAll(latLngList).color(Constant.LINE_COLOR).width(
                    Constant.LINE_WIDTH
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

    override fun onResume() {
        super.onResume()
        binding.mapMAPFullView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapMAPFullView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapMAPFullView.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapMAPFullView.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapMAPFullView.onLowMemory()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapMAPFullView.onSaveInstanceState(outState)

    }


}