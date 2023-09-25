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