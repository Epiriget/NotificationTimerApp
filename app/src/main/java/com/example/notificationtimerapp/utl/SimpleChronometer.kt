package com.example.notificationtimerapp.utl

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.core.os.postDelayed
import kotlinx.coroutines.delay

abstract class SimpleChronometer(val startTime: Long, val delayInMs: Long = 100) {

    private var handlerThread: HandlerThread? = null
    private var handler: Handler? = null
    private var currTime: Long = 0L
    private var isRunning:Boolean = false


    init {
        currTime = startTime
    }

    private val runnable = Runnable {
        currTime += delayInMs
    }

    suspend fun start() {
        handlerThread = HandlerThread("ThreadName")
        handlerThread?.start()
        isRunning = true
        handler = Handler(handlerThread!!.looper)

        while(isRunning) {
            handler?.post(runnable)
            onTickListener(currTime - startTime)
            delay(delayInMs)
        }
    }

    fun cancel() {
        handlerThread?.quit()
        handler?.removeCallbacksAndMessages(null)
        currTime = 0L
        isRunning = false
    }

    public abstract fun onTickListener(fromStartMs:Long)
}