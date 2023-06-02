package com.leothan522.demoapp.apis

import android.content.ContentValues
import android.util.Log
import android.widget.TextView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class FirebaseToken {
    fun getToken(tvToken: TextView) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Error al obtener el token de registro de FCM", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = "FCM-TOKEN ACTUAL: $token"
            Log.d(ContentValues.TAG, msg)
            tvToken.text = token
            //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
    }
}