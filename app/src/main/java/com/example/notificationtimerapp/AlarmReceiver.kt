package com.example.notificationtimerapp

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.notificationtimerapp.utl.sendNotification

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "SomeTxt from Alarm", Toast.LENGTH_SHORT).show()
        val notificationManager = ContextCompat.getSystemService(
            context, NotificationManager::class.java) as NotificationManager

        notificationManager.sendNotification(context.getString(R.string.notification_text), context)
    }
}
