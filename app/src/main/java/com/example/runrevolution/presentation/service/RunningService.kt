package com.example.runrevolution.presentation.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT

import android.content.Intent
import android.location.Location

import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.runrevolution.R
import com.example.runrevolution.domain.model.LocationDetails
import com.example.runrevolution.domain.repository.LocationRepository
import com.example.runrevolution.presentation.activity.StartActivity
import com.example.runrevolution.utils.other.Constant.FOREGROUND_CHANNEL_ID
import com.example.runrevolution.utils.other.Constant.TIMER_UPDATE_INTERVAL
import com.example.runrevolution.utils.other.TimeUtility


import com.example.runrevolution.data.db.LocationHandlerUtil
import com.example.runrevolution.domain.repository.LocationClient
import com.example.runrevolution.utils.other.Constant.LOCATION_NAME
import com.example.runrevolution.utils.other.Constant.MET_RATIO
import com.example.runrevolution.utils.other.Constant.RUNNING_
import com.example.runrevolution.utils.other.Constant.START_TIME

import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject




@AndroidEntryPoint
class RunningService : LifecycleService() {
    @Inject
    lateinit var repository: LocationRepository


    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private lateinit var locationClient: LocationClient
    private lateinit var notificationManager: NotificationManager
    private var previousLocation: Location? = null


    private val timeRunInMillis = MutableLiveData<Long>()
    private var lapTime = 0L
    private var timeRun = 0L
    private var timeStart = 0L
    private var lastSecond = 0L
    private val notification by lazy {
        NotificationCompat.Builder(this, LOCATION_NAME)
            .setContentTitle(RUNNING_)
            .setContentText(START_TIME)
            .setSmallIcon(R.drawable.ic_run)
            .setOngoing(true)
            .setAutoCancel(false)
            .setContentIntent(getStartActivityPendingIntent())
    }

    private fun startTimer() {
        timeStart = System.currentTimeMillis()
        CoroutineScope(Dispatchers.Main).launch {
            while (isRunning.value) {
                lapTime = System.currentTimeMillis() - timeStart
                timeRunInMillis.postValue(timeRun + lapTime)
                if (timeRunInMillis.value!! >= lastSecond + 1000L) {
                    timeInSeconds.postValue(timeInSeconds.value!! + 1)
                    lastSecond += 1000L
                }
                delay(TIMER_UPDATE_INTERVAL)
            }
            timeRun += lapTime
        }
    }

    private fun postInit() {
        timeInSeconds.postValue(0L)
        timeRunInMillis.postValue(0L)
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onCreate() {
        super.onCreate()
        postInit()
        locationClient = LocationHandlerUtil(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        when (intent?.action) {
            ACTION_START_SERVICE -> {
                if (!isRunning.value) {
                    start()
                }
            }

            ACTION_STOP_SERVICE -> stop()
        }
        return START_STICKY
    }

    private fun stop() {
        isRunning.value = false
        stopForeground(true)
        stopSelf()
    }

    private fun start() {
        isRunning.value = true
        startTimer()
        startLocationUpdates()
        startForegroundNotification()
        updateNotification()
    }

    private fun startLocationUpdates() {
        locationClient
            .getLocationUpdates(1L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                updateDistanceAndSpeed(location)
                repository.updateLocation(
                    LocationDetails(
                        location.latitude,
                        location.longitude,
                        location.time
                    )
                )
            }.launchIn(serviceScope)
    }

    private fun updateDistanceAndSpeed(location: Location) {
        if (previousLocation != null) {
            val distanceInMeters = previousLocation!!.distanceTo(location)
            val distanceInKilometers = distanceInMeters / 1000.0f // Convert to kilometers if needed
            totalDistance.postValue((totalDistance.value ?: 0f) + distanceInKilometers)
            Log.d("pttt", distanceInKilometers.toString())

            val timeElapsedInSeconds = (System.currentTimeMillis() - timeStart) / 1000
            val speedInKilometersPerHour = (distanceInKilometers / timeElapsedInSeconds) * 3600
            currentSpeed.postValue(speedInKilometersPerHour)
        }

        // Update previousLocation with the current location for the next calculation
        previousLocation = location
    }


    private fun updateNotification() {
        timeInSeconds.observe(this, Observer { ms ->
            notification.setContentText(TimeUtility.getFormattedTime(ms * 1000L)).let {
                notificationManager.notify(FOREGROUND_CHANNEL_ID, it.build())
            }
        })
    }

    private fun startForegroundNotification() {
        startForeground(FOREGROUND_CHANNEL_ID, notification.build())
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    private fun getStartActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, StartActivity::class.java).also {
            it.action = ACTION_SHOW_RUNNING_FRAGMENT
        },
        FLAG_UPDATE_CURRENT
    )

    companion object {
        const val ACTION_START_SERVICE = "ACTION_START"
        const val ACTION_STOP_SERVICE = "ACTION_STOP"
        const val ACTION_SHOW_RUNNING_FRAGMENT = "ACTION_SHOW_RUNNING_FRAGMENT"
        var isRunning = MutableStateFlow(false)
        val timeInSeconds = MutableLiveData<Long>()
        val totalDistance = MutableLiveData<Float>()

        //        val totalCalories = MutableLiveData<Double>()
        val currentSpeed = MutableLiveData<Float>()
    }


}










