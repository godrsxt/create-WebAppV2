package com.example.universalwebview

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    
    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Programmatic container initialization
        webView = WebView(this)
        setContentView(webView)

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            allowFileAccess = true
            allowFileAccessFromFileURLs = true
            allowUniversalAccessFromFileURLs = true
            
            // Native Engine Configurations
            builtInZoomControls = false  // Disable scaling completely
            displayZoomControls = false  // Remove system zoom UI elements
            useWideViewPort = true       // Lock layout viewport scale
            loadWithOverviewMode = true  // Auto fit large HTML objects
            cacheMode = WebSettings.LOAD_DEFAULT
        }

        // Kills the Android OS over-scroll edge bounce/glow effect entirely
        webView.overScrollMode = WebView.OVER_SCROLL_NEVER 

        // Internal routing engines
        webView.webViewClient = WebViewClient() 
        webView.webChromeClient = WebChromeClient() 

        // Asset Pipeline Link
        webView.loadUrl("file:///android_asset/index.html")

        // Native Hardware back-button logic routing to WebView History
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }
}
