package com.leothan522.demoapp.apis

import android.content.Context
import android.webkit.JavascriptInterface
import android.widget.Toast

class WebAppInterface(private val mContext: Context) {
    /** Show a toast from the web page  */
    @JavascriptInterface
    fun showToast(toast: String) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
    }
    @JavascriptInterface
    fun irLlamadas(telefono: String){
        Toast.makeText(mContext, "Telefono: $telefono", Toast.LENGTH_SHORT).show()
    }
}