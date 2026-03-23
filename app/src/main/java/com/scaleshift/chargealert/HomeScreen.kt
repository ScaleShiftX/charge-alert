package com.scaleshift.chargealert

import android.content.Context
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import android.media.RingtoneManager
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
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
    val menuWidth = 180f

    var showMenu by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .width(250.dp)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                label,
                style = MaterialTheme.typography.titleMedium
            )

            Box(
                modifier = Modifier.height(300.dp),
                contentAlignment = Alignment.TopStart
            ) {
                IconButton(
                    onClick = { showMenu = !showMenu }
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Menu"
                    )
                }

                if (showMenu) {
                    ElevatedCard(
                        modifier = Modifier
                            .padding(top = 40.dp)
                            .width(menuWidth.dp)
                            .height(300.dp), //to scroll
                        shape = RoundedCornerShape(4.dp),
                        elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = 8.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .verticalScroll(rememberScrollState()) //to scroll
                        ) {
                            //Generate list of available sounds dynamically
                            sounds.forEach { sound ->
                                DropdownMenuItem(
                                    text = { Text(sound.title) },
                                    onClick = {
                                        //Hide dropdown menu
                                        showMenu = false

                                        //Save selection
                                        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                                        prefs.edit {
                                            putString(preferenceKey, sound.uri.toString())
                                        }

                                        //Play preview sound
                                        val ringtone = RingtoneManager.getRingtone(context, sound.uri)
                                        ringtone?.play()
                                    }
                                )
                            }

                            //DropdownMenuItem(
                            //    text = { Text("Create New Item") },
                            //    onClick = { showMenu = false },
                            //    leadingIcon = {
                            //        Icon(Icons.Filled.Add, contentDescription = null)
                            //    }
                            //)
                        }
                    }
                }
            }
        }
    }
}