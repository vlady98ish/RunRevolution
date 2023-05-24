package com.example.runrevolution.utils.other

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.runrevolution.utils.other.Constant.NOTIFICATION_ID
import com.example.runrevolution.utils.other.Constant.NOTIFICATION_NAME

object NotificationUtils {
    // it creates a new notification channel with a specific ID, name, and importance level.
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_ID,
                NOTIFICATION_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            // Retrieve the NotificationManager system service and create a new notification channel
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }


}