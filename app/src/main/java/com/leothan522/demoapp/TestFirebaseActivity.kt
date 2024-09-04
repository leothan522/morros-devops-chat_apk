package com.leothan522.demoapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.leothan522.demoapp.apis.FirebaseToken
import com.leothan522.demoapp.databinding.ActivityTestFirebaseBinding
import com.leothan522.demoapp.prefs.SharedApp.Companion.prefs

class TestFirebaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestFirebaseBinding
    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestFirebaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            binding.verToken.text = msg
        })

        binding.btnCerrar.setOnClickListener {
            //prefs.wipe()
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
        }

    }
}