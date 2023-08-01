package com.app.vibespace.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.app.vibespace.R
import com.app.vibespace.ui.registration.HomeActivity
import com.app.vibespace.ui.registration.SignInActivity
import com.app.vibespace.util.MyApp.Companion.profileData

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        navigate()
    }

    private fun navigate() {

        Handler(Looper.getMainLooper()).postDelayed({
            if (profileData!=null)
                startActivity(Intent(this,HomeActivity::class.java))
            else
               startActivity(Intent(this,SignInActivity::class.java))
            finish()
        },2000)
    }
}