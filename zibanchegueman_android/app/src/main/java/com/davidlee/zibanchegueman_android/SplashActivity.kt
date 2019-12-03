package com.davidlee.zibanchegueman_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {

    private var mDelayeHandler : Handler? = null
    private  val SPLASH_DELAY : Long = 3000

    internal val  mRunnable = Runnable {
        if (!isFinishing){

            val intent=Intent(applicationContext, HubActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mDelayeHandler = Handler()

        mDelayeHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

    }

    public override fun onDestroy() {

        if(mDelayeHandler !=null){
            mDelayeHandler!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }
}
