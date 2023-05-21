package com.example.runrevolution.services

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.runrevolution.R
import com.example.runrevolution.utils.Constant.FOREGROUND_CHANNEL_ID

import com.example.runrevolution.utils.location.LocationHandlerUtil
import com.example.sosapplicaiton.utils.location.LocationClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LocationService: Service() {


    // This line initializes a new CoroutineScope for executing background tasks
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private lateinit var locationClient: LocationClient
//    private val locationRepository = LocationRepositoryImpl()
    private lateinit var notificationManager: NotificationManager

    private val notification by lazy {
        NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking location...")
            .setContentText("Location: null")
            .setSmallIcon(R.drawable.ic_run)
            .setOngoing(true)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("pttt", "FOREGROUND SERVICE BEFORE INITIALIZE CLIENT")
        locationClient = LocationHandlerUtil(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
        notificationManager = getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
    }

    // This function is called when the service is started with a command
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Start or stop the service based on the received action from the intent
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun stop() {
        Log.d("pttt", "FOREGROUND SERVICE STOPPED")
        stopForeground(true)
        stopSelf()
    }

    private fun start() {
        Log.d("pttt", "FOREGROUND SERVICE STARTED")
        startLocationUpdates()
        startForegroundNotification()
    }

//    private fun updateLocation(location: Location){
//        locationRepository.updateLocation(location)
//    }
    private fun startLocationUpdates() {
        // Start getting location updates from the location client
        locationClient
            .getLocationUpdates(10L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
//                updateLocation(location)
                updateNotification(location)
            }.launchIn(serviceScope)
    }

    private fun updateNotification(location: Location) {
        val lat = location.latitude.toString()
        val long = location.longitude.toString()
        notification.setContentText("Location ($lat, $long)").let {
            notificationManager.notify(FOREGROUND_CHANNEL_ID, it.build())
        }
    }


    private fun startForegroundNotification() {
        startForeground(FOREGROUND_CHANNEL_ID, notification.build())
    }

    // This function is called when the service is destroyed
    override fun onDestroy() {
        super.onDestroy()
        // Cancel the service scope to stop all the background tasks
        serviceScope.cancel()
    }

    // This companion object contains the actions that can be sent to the service through an intent
    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}