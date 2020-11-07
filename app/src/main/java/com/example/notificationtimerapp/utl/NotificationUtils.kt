package com.example.notificationtimerapp.utl

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.notificationtimerapp.MainActivity
import com.example.notificationtimerapp.R

private val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(message: String, applicationContext: Context) {
    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(applicationContext,
    applicationContext.getString(R.string.notification_channel_id))
        .setSmallIcon(R.drawable.ic_timer)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(message)
        .setContentIntent(contentPendingIntent)
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(NOTIFICATION_ID, builder.build())
}