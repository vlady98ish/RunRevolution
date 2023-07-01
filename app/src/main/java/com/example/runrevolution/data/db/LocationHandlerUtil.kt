package com.example.runrevolution.data.db

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import com.example.runrevolution.domain.repository.LocationClient
import com.example.runrevolution.utils.location.hasLocationPermission

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class LocationHandlerUtil(
    private val context: Context,
    private val client: FusedLocationProviderClient
) : LocationClient {
    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(interval: Long): Flow<Location> {
        return callbackFlow {
            checkLocationPermission();

            checkLocationProviders();

            val request = buildLocationRequest(interval);

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    result.locations.lastOrNull()?.let { location ->

                        launch { send(location) }
                    }
                }
            }

            client.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                client.removeLocationUpdates(locationCallback)
            }
        }
    }

    private fun checkLocationPermission() {
        if (!context.hasLocationPermission()) {
            throw Exception("Missing location permission")
        }
    }


    private fun checkLocationProviders() {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)


        if (!isGpsEnabled && !isNetworkEnabled) {
            throw Exception("GPS is disabled")
        }
    }


    private fun buildLocationRequest(interval: Long): LocationRequest {
        return LocationRequest.Builder(interval)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMinUpdateDistanceMeters(1.0f)
            .setMinUpdateIntervalMillis(interval)
            .setMaxUpdateDelayMillis(TimeUnit.MINUTES.toMillis(1))
            .build()
    }


}