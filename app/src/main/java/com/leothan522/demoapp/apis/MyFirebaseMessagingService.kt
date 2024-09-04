package com.leothan522.demoapp.apis

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.leothan522.demoapp.ChatActivity
import com.leothan522.demoapp.LoginActivity
import com.leothan522.demoapp.R
import com.leothan522.demoapp.SplashActivity
import com.leothan522.demoapp.TestFirebaseActivity
import com.leothan522.demoapp.prefs.SharedApp.Companion.prefs

class MyFirebaseMessagingService : FirebaseMessagingService() {
    // IT IS CALLED WHEN THE TOKEN IS CREATED
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val notification = message.notification
        val data = message.data

        var title: String
        var body: String
        var image: String
        var destino: Boolean = false
        var text: String

        //With Notification
        if (notification != null) {

            title = notification.title ?: "Title NO Found"
            body = notification.body ?: "Body NO Found"
            image = notification.imageUrl!!.toString()

            //With DATA
            if (prefs.getLogin()) {
                if (data.isNotEmpty()) {
                    //subtext = data["subText"].toString()
                    destino = true;
                }
            }

            sendNotification(title, body, image, destino)
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification(title: String, message: String, subtext: String, destino: Boolean) {

        val intent: Intent = if (destino) {
            Intent(this, TestFirebaseActivity::class.java)
        } else {
            Intent(this, SplashActivity::class.java)
        }

        //solo unciona al estar abierta la app
        val pendingIntent = PendingIntent.getActivity(
            this,
            MyNotification.NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.logo_android)
        val smallIcon = R.drawable.ic_circle_notifications
        val notification = MyNotification(this, MyNotification.CHANNEL_ID_NOTIFICATIONS)

        notification.build(largeIcon, smallIcon, title, message, subtext, pendingIntent)
        notification.addChannel(getText(R.string.notification_channel_name))
        notification.createChannelGroup(
            MyNotification.CHANNEL_GROUP_GENERAL,
            R.string.notification_channel_group_general
        )

        notification.show(MyNotification.NOTIFICATION_ID)
    }


}