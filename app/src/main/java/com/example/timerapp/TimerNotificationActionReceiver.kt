package com.example.timerapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.timerapp.util.NotificationUtil
import com.example.timerapp.util.PrefUtil

class TimerNotificationActionReceiver : BroadcastReceiver() {

    companion object {
        const val TAG = "TimerNAReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            AppConstants.ACTION_STOP -> {
                MainActivity.removeAlarm(context)
                PrefUtil.setTimerState(MainActivity.TimerState.Stopped, context)
                NotificationUtil.hideTimerNotification(context)
            }

            AppConstants.ACTION_PAUSE -> {
                var secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val alarmSetTime = PrefUtil.getAlarmSetTime(context)
                val nowSeconds = MainActivity.nowSeconds

                secondsRemaining -= nowSeconds - alarmSetTime
                PrefUtil.setSecondsRemaining(secondsRemaining, context)

                MainActivity.removeAlarm(context)
                PrefUtil.setTimerState(MainActivity.TimerState.Paused, context)
                NotificationUtil.showTimerPaused(context)
            }

            AppConstants.ACTION_RESUME -> {
                var secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val wakeUpTime = MainActivity.setAlarm(context, MainActivity.nowSeconds, secondsRemaining)

                PrefUtil.setTimerState(MainActivity.TimerState.Running, context)
//                NotificationUtil.showTimerRunning(context, wakeUpTime)
                NotificationUtil.showTimerRunning(context, secondsRemaining)
            }

            AppConstants.ACTION_START -> {
                Log.d(TAG, "ACTION_START")
                val minutesRemaining = PrefUtil.getTimerLength(context)
                val secondsRemaining = minutesRemaining * 60L
                Log.d(TAG, "" + secondsRemaining)
                val wakeUpTime = MainActivity.setAlarm(context, MainActivity.nowSeconds, secondsRemaining)

                PrefUtil.setTimerState(MainActivity.TimerState.Running, context)
                PrefUtil.setSecondsRemaining(secondsRemaining, context)

//                NotificationUtil.showTimerRunning(context, wakeUpTime)
                NotificationUtil.showTimerRunning(context, secondsRemaining)

            }
        }

    }
}
