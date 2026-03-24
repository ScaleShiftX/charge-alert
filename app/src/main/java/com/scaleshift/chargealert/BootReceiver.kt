package com.scaleshift.chargealert

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            //When the phone boots up, start the alerts service
            val serviceIntent = Intent(context, Alerts::class.java)
            context.startForegroundService(serviceIntent) //Works on emulator, does not work on device
            //ContextCompat.startForegroundService(context, serviceIntent) //Should work on device

            //We actually have two places where the service can be started:
            //Opening up the app: MainActivity.onCreate(): startForegroundService()
            //Booting up the phone: BootReceiver.onReceive(): startForegroundService()
            //startForegroundService() is not idempotent, but it won't create two services
            //It will just call onStartCommand() again, which should be fine

            Log.d("ChargeAlert", "BootReceiver fired")
        }
    }
}