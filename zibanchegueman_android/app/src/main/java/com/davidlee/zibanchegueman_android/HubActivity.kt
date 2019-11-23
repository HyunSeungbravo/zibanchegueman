package com.davidlee.zibanchegueman_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.davidlee.zibanchegueman_android.R
import com.davidlee.zibanchegueman_android.func.ChangeStatusBarColorFunc
import kotlinx.android.synthetic.main.activity_hub.*

class HubActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ChangeStatusBarColorFunc.updateStatusBarColor(this, R.color.colorWhite)
        setContentView(R.layout.activity_hub)


        init_buttons()
    }

    fun init_buttons(){

        student_accounting.setOnClickListener {

            startActivity(Intent(this, AccountActivity::class.java))

        }
    }

}
