package com.app.vibespace.ui.profile

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import com.app.vibespace.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileActivity : AppCompatActivity() {
    private var navHostFragment: NavHostFragment?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_user_profile)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.navUserFragment) as NavHostFragment?
    }

    override fun onBackPressed() {
        if(navHostFragment!=null && navHostFragment!!.childFragmentManager.backStackEntryCount>0)
            navHostFragment!!.childFragmentManager.popBackStack()
        else {

            finish()
        }
    }
}