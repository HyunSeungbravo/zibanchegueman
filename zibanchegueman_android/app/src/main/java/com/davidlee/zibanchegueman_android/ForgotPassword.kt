package com.davidlee.zibanchegueman_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.davidlee.zibanchegueman_android.func.ChangeStatusBarColorFunc

class ForgotPassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ChangeStatusBarColorFunc.updateStatusBarColor(this, R.color.colorWhite)
        setContentView(R.layout.activity_forgot_password)
    }
}
