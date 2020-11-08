package com.example.notificationtimerapp.utl
import android.text.format.DateUtils
import android.widget.TextView

fun TextView.setElapsedTime(value:Long) {
    val seconds = (value / 1000) % 60
    text = DateUtils.formatElapsedTime(seconds)
}