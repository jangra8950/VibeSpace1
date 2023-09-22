package com.app.vibespace.ui.settings

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.app.vibespace.R
import com.app.vibespace.ui.registration.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : AppCompatActivity() {
    private var navHostFragment: NavHostFragment?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = window.decorView
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setContentView(R.layout.activity_setting)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.navSettingFragment) as NavHostFragment?
    }

    override fun onBackPressed() {
        if(navHostFragment!=null && navHostFragment!!.childFragmentManager.backStackEntryCount>0)
            navHostFragment!!.childFragmentManager.popBackStack()
        else {

            finish()
        }
    }

}