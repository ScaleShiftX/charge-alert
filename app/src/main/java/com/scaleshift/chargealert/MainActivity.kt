package com.scaleshift.chargealert

import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.scaleshift.chargealert.ui.theme.ChargeAlertTheme

const val PREFS_NAME = "charge_alert_prefs"
const val KEY_CONNECTED = "connected_sound_uri"
const val KEY_DISCONNECTED = "disconnected_sound_uri"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //Start Alerts service
        val intent = Intent(this, Alerts::class.java)
        startForegroundService(intent)

        ////Register Alerts as a broadcast receiver (this method only works while the app is open)
        //val alerts = Alerts()
        //val intentFilter = IntentFilter()
        //intentFilter.addAction(Intent.ACTION_POWER_CONNECTED)
        //intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED)
        //this.registerReceiver(alerts, intentFilter)

        //Display
        setContent {
            ChargeAlertTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ChargeAlertHomeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}