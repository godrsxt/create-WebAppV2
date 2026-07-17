package com.example.universalwebview

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class MainActivity : AppCompatActivity() {
    
    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // ==========================================
        // 1. ABSOLUTE FULL SCREEN (Fixes Black Bars)
        // ==========================================
        // Force the app to draw around and underneath the camera notch
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        
        // Completely hide the system navigation bar (swipe bar) and status bar
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        webView = WebView(this)
        setContentView(webView)

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            allowFileAccess = true
            allowFileAccessFromFileURLs = true
            allowUniversalAccessFromFileURLs = true
            
            // ==========================================
            // 2. ABSOLUTELY KILL ALL ZOOMING
            // ==========================================
            setSupportZoom(false) // <--- The silver bullet that stops multi-touch pinch zooming
            builtInZoomControls = false  
            displayZoomControls = false  
            useWideViewPort = true       
            loadWithOverviewMode = true  
            cacheMode = WebSettings.LOAD_DEFAULT
        }

        webView.overScrollMode = WebView.OVER_SCROLL_NEVER 

        webView.webViewClient = WebViewClient() 
        webView.webChromeClient = WebChromeClient() 
        webView.loadUrl("file:///android_asset/index.html")

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

        // ==========================================
        // 3. ASK FOR RUNTIME PERMISSIONS
        // ==========================================
        requestRuntimePermissions()
    }

    private fun requestRuntimePermissions() {
        // Here you define which permissions the app asks the user for on startup.
        // Simply uncomment the ones you also uncommented in AndroidManifest.xml.
        val permissionsNeeded = arrayOf(
            // Manifest.permission.CAMERA,
            // Manifest.permission.RECORD_AUDIO,
            // Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        // Checks which ones the user hasn't accepted yet
        val permissionsToRequest = permissionsNeeded.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        // Triggers the native Android pop-up asking for permission
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest, 101)
        }
    }
}
