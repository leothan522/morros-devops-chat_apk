package com.leothan522.demoapp.apis

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.leothan522.demoapp.R

class MyNotification(private val context: Context, private val channelId: String) {

    private val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(context, channelId)
    private val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private var channel: NotificationChannel? = null

    fun build(
        largeIcon: Bitmap,
        smallIcon: Int,
        title: String?,
        content: String?,
        subText: String?,
        pendingIntent: PendingIntent
    ) {

        notificationBuilder
            .setLargeIcon(largeIcon) // (4)
            .setSmallIcon(smallIcon)
            .setColor(context.resources.getColor(R.color.azul_logo))
            .setContentTitle(title)
            .setContentText(content)
            .setSubText(subText) // (8)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            //.addAction(R.drawable.ic_bookmark,"Leer más tarde", null) // (10)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(content))
    }

    fun createChannelGroup(groupId: String?, groupNameId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val groupName: CharSequence = context.getString(groupNameId)
            notificationManager.createNotificationChannelGroup(NotificationChannelGroup(groupId, groupName))
            channel!!.group = groupId
        }
    }

    fun addChannel(chanelName: CharSequence?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = NotificationChannel(channelId, chanelName, NotificationManager.IMPORTANCE_DEFAULT)
        }
    }

    fun show(idAlert: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel!!)
        }
        notificationManager.notify(idAlert, notificationBuilder.build())
    }

    fun setVibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel!!.enableVibration(true)
            channel!!.vibrationPattern = longArrayOf(1000, 1000)
        } else notificationBuilder.setVibrate(longArrayOf(1000, 1000))
    }

    fun setSound(sound: Uri?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .build()
            channel!!.setSound(sound, audioAttributes)
        } else notificationBuilder.setSound(sound)
    }

    companion object {
        const val CHANNEL_ID_NOTIFICATIONS = "channel_id_notifications"
        const val CHANNEL_GROUP_GENERAL = "channel_group_general"
        const val NOTIFICATION_ID = 1
    }

}