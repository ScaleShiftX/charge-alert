package com.scaleshift.chargealert

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri

data class SoundItem(
    val title: String,
    val uri: Uri
)

fun getNotificationSounds(context: Context): List<SoundItem> {
    val manager = RingtoneManager(context)
    manager.setType(RingtoneManager.TYPE_NOTIFICATION)

    val cursor = manager.cursor
    val sounds = mutableListOf<SoundItem>()

    while (cursor.moveToNext()) {
        val title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
        val uri = manager.getRingtoneUri(cursor.position)

        sounds.add(SoundItem(title, uri))
    }

    cursor.close()
    return sounds
}