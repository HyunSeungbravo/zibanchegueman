package com.davidlee.zibanchegueman_android.navigation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.davidlee.zibanchegueman_android.BlockInfoActivity
import com.davidlee.zibanchegueman_android.R
import kotlinx.android.synthetic.main.activity_block_info.*
import kotlinx.android.synthetic.main.activity_block_info.real_block_view
import kotlinx.android.synthetic.main.fragment_real_block_info.*

class RealBlockInfoFragment : Fragment(){

    var fragmentView : View? = null
    val url1 = "http://35.235.52.246:8080/web/android/index.html"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getBlockInfo()

        init_btns()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        fragmentView = LayoutInflater.from(activity).inflate(R.layout.fragment_real_block_info, container, false)


        return fragmentView
    }


    fun getBlockInfo(){
        val settings = real_block_view.settings

        settings.javaScriptEnabled = true
        //block_view.addJavascriptInterface(WebBrideg(), "java")



        //if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        //  settings.safeBrowsingEnabled = true
        //}


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
        //settings.setCacheMode(WebSettings.LOAD_NO_CACHE)

        settings.domStorageEnabled = true


        real_block_view.loadUrl(url1)


    }

    fun init_btns(){

        block_refresh.setOnClickListener {
            real_block_view.loadUrl(url1)
        }

        block_realdata.setOnClickListener {
            startActivity(Intent(view!!.context, BlockInfoActivity::class.java))
        }
    }





//////////
}
