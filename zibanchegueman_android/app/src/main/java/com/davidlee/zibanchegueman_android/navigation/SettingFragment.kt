package com.davidlee.zibanchegueman_android.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.davidlee.zibanchegueman_android.R

class SettingFragment : Fragment() {

    var fragmentView : View? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentView = LayoutInflater.from(activity).inflate(R.layout.fragment_setting, container, false)


        return fragmentView
    }








/////////
}