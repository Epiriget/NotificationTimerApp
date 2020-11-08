package com.example.notificationtimerapp

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.notificationtimerapp.utl.sendNotification
import com.example.notificationtimerapp.utl.setElapsedTime
import java.lang.StringBuilder
import java.time.format.DateTimeFormatter

class TimerFragment : Fragment() {

    private val viewModel: TimerViewModel by viewModels()
    companion object {
        fun newInstance() = TimerFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_timer, container, false)

        val textview = view.findViewById<TextView>(R.id.timer)
        val timeObserver = Observer<Long> {time -> textview.setElapsedTime(time) }
        viewModel.elapsedTime.observe(requireActivity(), timeObserver)


        val startButton = view.findViewById<Button>(R.id.start_button)
        val resetButton = view.findViewById<Button>(R.id.reset_button)


        startButton.setOnClickListener { viewModel.setAlarm(true) }
        resetButton.setOnClickListener { viewModel.setAlarm(false) }

        createNotificationChannel(getString(R.string.notification_channel_id),
            getString(R.string.notification_channel_name))

        return view
    }

    private fun createNotificationChannel(channelId:String, channelName:String) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.CYAN
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.notification_channel_description)

            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

}
