package com.davidlee.zibanchegueman_android

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.*
import com.davidlee.zibanchegueman_android.func.ChangeStatusBarColorFunc
import kotlinx.android.synthetic.main.activity_account.*
import android.webkit.JavascriptInterface as JavascriptInterface1

class AccountActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ChangeStatusBarColorFunc.updateStatusBarColor(this, R.color.colorWhite)
        setContentView(R.layout.activity_account)

        val settings = block_view.settings

        settings.javaScriptEnabled = true
        //block_view.addJavascriptInterface(WebBrideg(), "java")



        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            settings.safeBrowsingEnabled = true
        }


        //settings.loadWithOverviewMode = true
        settings.javaScriptCanOpenWindowsAutomatically = true

        settings.setUseWideViewPort(true)
        // 줌 컨트롤 사용 여부
        settings.setDisplayZoomControls(false)

        // TextEncoding 이름 정의
        settings.setDefaultTextEncodingName("UTF-8")

        // Setting Local Storage
        settings.setDatabaseEnabled(true)
        settings.setDomStorageEnabled(true)

        // 캐쉬 사용 방법을 정의
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE)

        settings.domStorageEnabled = true


        block_view.loadUrl("http://35.235.52.246:8080/web/android/index.html")

    }



}
