package com.scaleshift.chargealert

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            //When the phone boots up, start the alerts service
            val serviceIntent = Intent(context, Alerts::class.java)
            context.startForegroundService(serviceIntent)

            //We actually have two places where the service can be started:
            //Opening up the app: MainActivity.onCreate(): startForegroundService()
            //Booting up the phone: BootReceiver.onReceive(): startForegroundService()
            //startForegroundService() is not idempotent, but it won't create two services
            //It will just call onStartCommand() again, which is fine as we aren't overriding that method anyway

            Log.d("ChargeAlert", "BootReceiver fired")
        }
    }
}