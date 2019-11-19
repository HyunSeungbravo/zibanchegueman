package com.davidlee.zibanchegueman_android.func

import android.app.Activity
import android.os.Build
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class ChangeStatusBarColorFunc : AppCompatActivity(){

    companion object {

        fun updateStatusBarColor (context: Activity, colorId:Int) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                var window = context.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(context, colorId));

            }
        }
    }
}