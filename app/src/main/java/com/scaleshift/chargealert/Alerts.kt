package com.scaleshift.chargealert

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.net.toUri

class Alerts : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        //Set default sounds
        val prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        //Connected
        val connectedUriString = prefs?.getString(KEY_CONNECTED, null)
        val connectedUri = connectedUriString?.toUri() ?:
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val connectedRingtone = RingtoneManager.getRingtone(context, connectedUri)

        //Disconnected
        val disconnectedUriString = prefs?.getString(KEY_DISCONNECTED, null)
        val disconnectedUri = disconnectedUriString?.toUri() ?:
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val disconnectedRingtone = RingtoneManager.getRingtone(context, disconnectedUri)

        ////Connected
        //val connectedSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        //val connectedRingtone = RingtoneManager.getRingtone(context, connectedSoundUri)
        //
        ////Disconnected
        //val disconnectedSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        //val disconnectedRingtone = RingtoneManager.getRingtone(context, disconnectedSoundUri)

        //When plugged in or unplugged
        when (intent?.action) {
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