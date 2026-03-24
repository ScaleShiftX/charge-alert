package com.scaleshift.chargealert

import android.content.Context
import android.media.RingtoneManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.scaleshift.chargealert.ui.theme.ChargeAlertTheme
import androidx.core.content.edit

@Composable
fun ChargeAlertHomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Charge Alert",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Plays a sound when charging starts or stops.",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        SoundDropdownMenu("Connected sound", KEY_CONNECTED)
        SoundDropdownMenu("Disconnected sound", KEY_DISCONNECTED)
    }
}

@Preview(showBackground = true)
@Composable
fun ChargeAlertHomeScreenPreview() {
    ChargeAlertTheme {
        ChargeAlertHomeScreen()
    }
}

@Composable
fun SoundDropdownMenu(
    label: String,
    preferenceKey: String
) {
    val context = LocalContext.current

    //Available sounds
    val sounds = remember { getNotificationSounds(context) }

    //Menu display
    var showMenu by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    label,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                Box {
                    IconButton(
                        onClick = { showMenu = !showMenu }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Menu"
                        )
                    }


                    if (showMenu) {
                        DropdownMenu(
                            expanded = true,
                            onDismissRequest = { showMenu = false }
                        ) {
                            sounds.forEach { sound ->
                                //A dropdown menu item is created for every notification sound on the system
                                DropdownMenuItem(
                                    text = { Text(sound.title) },
                                    onClick = {
                                        showMenu = false

                                        val prefs = context.getSharedPreferences(
                                            PREFS_NAME,
                                            Context.MODE_PRIVATE
                                        )
                                        prefs.edit {
                                            putString(preferenceKey, sound.uri.toString())
                                        }

                                        val ringtone =
                                            RingtoneManager.getRingtone(context, sound.uri)
                                        ringtone?.play()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}