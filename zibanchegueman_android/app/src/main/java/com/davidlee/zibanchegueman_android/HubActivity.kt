package com.davidlee.zibanchegueman_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import com.davidlee.zibanchegueman_android.R
import com.davidlee.zibanchegueman_android.func.ChangeStatusBarColorFunc
import com.davidlee.zibanchegueman_android.navigation.AccountingFragment
import com.davidlee.zibanchegueman_android.navigation.RealBlockInfoFragment
import com.davidlee.zibanchegueman_android.navigation.SettingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_hub.*

class HubActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {

        when (p0.itemId){

            R.id.navigation_Accounting ->{

                var accounting = AccountingFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content, accounting).commit()

                return true
            }

            R.id.navigation_RealBlockInfo ->{

                var realblockinfo = RealBlockInfoFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content, realblockinfo).commit()

                return true
            }

            R.id.navigation_Settings ->{

                var settings = SettingFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content, settings).commit()

                return true
            }

        }
        return false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ChangeStatusBarColorFunc.updateStatusBarColor(this, R.color.colorWhite)
        setContentView(R.layout.activity_hub)
        bottom_navigation.setOnNavigationItemSelectedListener(this)

        bottom_navigation.selectedItemId = R.id.navigation_Accounting


        //init_buttons()
    }



}
