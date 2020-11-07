package com.example.notificationtimerapp

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.os.SystemClock
import android.widget.Chronometer
import androidx.core.app.AlarmManagerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TimerViewModel(private val app: Application): AndroidViewModel(app) {

    private val START_TIME: String = "CHRONOMETER_START_TIME"
    private val REQUEST_CODE = 0

    private val notifyPendingIntent: PendingIntent
    private val notifyIntent: Intent = Intent(app, AlarmReceiver::class.java)

    private val minute = 60_000L
    private val second = 1_000L

    private val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private var prefs = app.getSharedPreferences("com.example.notificationtimerapp", Context.MODE_PRIVATE)

    //Todo: check whether LiveData is being used in proper way
    private val _elapsedTime = MutableLiveData<Long>()
    val elapsedTime: LiveData<Long>
        get() = _elapsedTime

    private val _chronometerOn = MutableLiveData<Boolean>()
    val chronometerOn: LiveData<Boolean>
        get() = _chronometerOn

    // Todo: replace with self-made implementation
    private lateinit var timer: CountDownTimer

    init {

        // Todo: check how FLAG_NO_CREATE exactly works
        _chronometerOn.value = false/*PendingIntent.getBroadcast(getApplication(),
        REQUEST_CODE,
        notifyIntent,
        PendingIntent.FLAG_NO_CREATE) != null
*/
        notifyPendingIntent = PendingIntent.getBroadcast(
            getApplication(),
            REQUEST_CODE,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun createChronometer() {
        viewModelScope.launch {
            val startTime = loadStartTime()

            timer = object : CountDownTimer(startTime + minute, second) {
                override fun onTick(millisUntilFinished: Long) {
                    _elapsedTime.value = SystemClock.elapsedRealtime() - startTime
                }

                override fun onFinish() {
                    resetChronometer()
                }
            }.start()
        }
    }

    private fun cancelNotification() {
        resetChronometer()
        alarmManager.cancel(notifyPendingIntent)
    }

    private fun resetChronometer() {
        timer.cancel()
        _elapsedTime.value = 0
        _chronometerOn.value = false
    }

    fun setAlarm(isChecked: Boolean) {
        if(isChecked) {
            startChronometer()
        } else {
            cancelNotification()
        }
    }

    // Call it in case of starting bg task
    private fun startChronometer() {
            if(_chronometerOn.value == false) {
                _chronometerOn.value = true

                val startTime = SystemClock.elapsedRealtime()

                //Todo: cancel all existing notifications

                //Todo: replace to WorkManager
                AlarmManagerCompat.setExactAndAllowWhileIdle(
                    alarmManager,
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    startTime + minute,
                    notifyPendingIntent
                )

                viewModelScope.launch {
                    saveStartTime(startTime)
                }
                createChronometer()
            }
    }

    private suspend fun saveStartTime(startTime: Long) {
        withContext(Dispatchers.IO) {
            prefs.edit().putLong(START_TIME, startTime).apply()
        }
    }

    private suspend fun loadStartTime(): Long =
        withContext(Dispatchers.IO) {
            prefs.getLong(START_TIME, 0)
        }

}