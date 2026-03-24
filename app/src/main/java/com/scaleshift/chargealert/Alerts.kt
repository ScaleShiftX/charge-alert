package com.scaleshift.chargealert

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri

class Alerts : Service() {
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("ChargeAlert", "BroadcastReceiver().onReceive called!")

            //Set default sounds
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

            //Connected
            val connectedUriString = prefs?.getString(KEY_CONNECTED, null)
            val connectedUri = connectedUriString?.toUri() ?: RingtoneManager.getDefaultUri(
                RingtoneManager.TYPE_NOTIFICATION
            )

            val connectedRingtone = RingtoneManager.getRingtone(context, connectedUri)

            //Disconnected
            val disconnectedUriString = prefs?.getString(KEY_DISCONNECTED, null)
            val disconnectedUri = disconnectedUriString?.toUri() ?: RingtoneManager.getDefaultUri(
                RingtoneManager.TYPE_NOTIFICATION
            )

            val disconnectedRingtone = RingtoneManager.getRingtone(context, disconnectedUri)

            ////Connected
            //val connectedSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            //val connectedRingtone = RingtoneManager.getRingtone(context, connectedSoundUri)
            //
            ////Disconnected
            //val disconnectedSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            //val disconnectedRingtone = RingtoneManager.getRingtone(context, disconnectedSoundUri)

            //When plugged in or unplugged
            when (intent.action) {
                Intent.ACTION_POWER_CONNECTED -> {
                    Log.d("ChargeAlert", "Power connected")
                    connectedRingtone?.play()
                }

                Intent.ACTION_POWER_DISCONNECTED -> {
                    Log.d("ChargeAlert", "Power disconnected")
                    disconnectedRingtone?.play()
                }
            }
        }
    }

    //An arbitrary foreground service to prevent optimization from preventing this app from running in the background
    override fun onCreate() {
        super.onCreate()

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }

        registerReceiver(receiver, filter)

        //Required foreground notification
        startForeground(1, createNotification())
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?) = null

    //Mock notification
    private fun createNotification(): Notification {
        val channelId = "power_service_channel"

        // Create channel (required for Android 8+)
        val channel = NotificationChannel(
            channelId,
            "Power Monitoring Service",
            NotificationManager.IMPORTANCE_LOW
        )

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Charge Alert")
            .setContentText("Monitoring power connection")
            .setSmallIcon(android.R.drawable.ic_lock_idle_charging)
            .build()
    }
}