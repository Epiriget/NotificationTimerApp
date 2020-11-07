package com.example.notificationtimerapp.utl
import android.text.format.DateUtils
import android.widget.TextView

fun TextView.setElapsedTime(value:Long) {
    // 1/10 of second
    val mSeconds = (value / 100) % 10
    val seconds = (value / 1000) % 60
    val minutes = (value / 60_000)
    text = DateUtils.formatElapsedTime(seconds)
}