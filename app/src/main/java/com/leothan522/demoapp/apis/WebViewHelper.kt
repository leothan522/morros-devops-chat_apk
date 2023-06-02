package com.leothan522.demoapp.apis

import android.annotation.SuppressLint
import android.app.Activity
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.leothan522.demoapp.MainActivity
import com.leothan522.demoapp.R
import com.leothan522.demoapp.dialogs.Dialogs

object WebViewHelper {
    @SuppressLint("SetJavaScriptEnabled")
    fun webView(
        webView: WebView,
        url: String,
        activity: Activity?,
        loading: ConstraintLayout,
        swipe: SwipeRefreshLayout
    ){
        webView.addJavascriptInterface(WebAppInterface(webView.context), "Android")
        webView.loadUrl(url)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                //Toast.makeText(webView.context, "NO INTERNET: $error", Toast.LENGTH_LONG).show()
                //(activity as MainActivity?)!!.onBackPressed()
                webView.isVisible = false
                Dialogs().noInternetMain(
                    webView.context,
                    activity!!.layoutInflater.inflate(R.layout.dialog_no_internet, null),
                    activity!!
                )
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                swipe.isRefreshing = false
            }
        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress > 50){
                    loading.isVisible = false
                }
            }
        }
        webView.setOnKeyListener(View.OnKeyListener { v, keyCode, event -> //This is the filter
            if (event.action !== KeyEvent.ACTION_DOWN) return@OnKeyListener true
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    (activity as MainActivity?)!!.onBackPressed()
                }
                return@OnKeyListener true
            }
            false
        })
    }
}