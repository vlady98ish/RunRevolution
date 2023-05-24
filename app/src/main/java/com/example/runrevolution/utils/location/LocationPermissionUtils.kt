package com.example.runrevolution.utils.location

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale

object LocationPermissionUtils {
    private const val PERMISSION_REQUEST_CODE = 123


    fun checkLocationPermissions(context: Context): Boolean {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        val granted = PackageManager.PERMISSION_GRANTED

        return ActivityCompat.checkSelfPermission(context, permission) == granted
    }

    fun requestLocationPermission(activity: Activity) {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(permission),
            PERMISSION_REQUEST_CODE
        )
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
        activity: Activity,
        onPermissionResult: (Boolean) -> Unit

    ) {
        var allPermissionsGranted = false
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            allPermissionsGranted = true


        } else {
            if (!shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                showPermissionDialog(activity)
            } else {
                showRationaleDialog(activity)
            }

        }
        onPermissionResult(allPermissionsGranted)
    }

    private fun showPermissionDialog(activity: Activity) {
        AlertDialog.Builder(activity)
            .setMessage("Location permission is required to access the app. Please enable it in the app settings.")
            .setPositiveButton("Settings") { _, _ ->
                openAppSettings(activity)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun openAppSettings(activity: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", "com.example.runrevolution", null)
        intent.data = uri
        activity.startActivity(intent)
    }


    private fun showRationaleDialog(activity: Activity) {
        AlertDialog.Builder(activity)
            .setTitle("Location Permission Required")
            .setMessage("This app requires access to your location to provide accurate distance tracking during your run.")
            .setPositiveButton("Grant Permission") { _, _ ->
                // Request the permission again
                requestLocationPermission(activity)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


}