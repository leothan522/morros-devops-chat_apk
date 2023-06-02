package com.leothan522.demoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.leothan522.demoapp.apis.Direcciones
import com.leothan522.demoapp.apis.WebViewHelper
import com.leothan522.demoapp.databinding.ActivityChatBinding
import com.leothan522.demoapp.prefs.SharedApp.Companion.prefs

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loading = binding.layoutWeb.layoutLoading.loading
        val swipe = binding.layoutWeb.swipe
        val webView = binding.layoutWeb.webView
        loading.isVisible = true;
        WebViewHelper.webView(webView, Direcciones().URL_CHAT, this, loading, swipe)
        swipe.setOnRefreshListener {
            webView.reload()
            //swipe.isRefreshing = false
        }

        binding.buttonCerrar.setOnClickListener {
            prefs.wipe();
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }
}