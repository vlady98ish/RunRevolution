package com.example.runrevolution

import android.app.Application
import com.example.runrevolution.utils.other.NotificationUtils
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class RunApplication : Application() {

    companion object {
        lateinit var instance: RunApplication
            private set
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        NotificationUtils.createNotificationChannel(this)
    }


}