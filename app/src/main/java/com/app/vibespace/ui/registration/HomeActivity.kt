package com.app.vibespace.ui.registration

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.R
import com.app.vibespace.databinding.ActivityHomeBinding
import com.app.vibespace.databinding.FragmentHomeBinding
import com.app.vibespace.ui.profile.ProfileHostFragment
import com.app.vibespace.ui.profile.ProfileMainFragment
import com.app.vibespace.util.CommonFuctions
import com.app.vibespace.util.MyApp
import com.app.vibespace.util.MyApp.Companion.profileData
import com.app.vibespace.util.MyApp.Companion.sharedpreferences

import com.app.vibespace.util.showToast
import com.app.vibespace.util.toast

import com.app.vibespace.viewModel.registration.HomeViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val model:HomeViewModel by viewModels()
    private var navHostFragment: NavHostFragment?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = window.decorView
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        binding=DataBindingUtil.setContentView(this,R.layout.activity_home)
        Log.i("SAHIL_DATA",Gson().toJson(profileData))

       // binding.tvName.text="${profileData?.firstName} ${profileData?.lastName}"

        navHostFragment = supportFragmentManager.findFragmentById(R.id.navFragment) as NavHostFragment?

        setFragment()

    }




    private fun setFragment() {

        val firstFragment=ProfileHostFragment()
        val secondFragment=VerifyOtpFragment()
        val thirdFragment=VerifyEmailFragment()
        val fourthFragment=VerificationSuccessFragment()

        setCurrentFragment(firstFragment)


        binding.navigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.map->{
                    setCurrentFragment(thirdFragment)
                    true}
                R.id.feed->{setCurrentFragment(secondFragment)
                    true}
                R.id.profile->{setCurrentFragment(firstFragment)
                    true}
                R.id.chat->{setCurrentFragment(fourthFragment)
                    true}
                else->false
            }

        }

    }

    private fun setCurrentFragment(firstFragment: Fragment) {
        val transcation=supportFragmentManager.beginTransaction()
        transcation.replace(R.id.flFragment,firstFragment)
        transcation.commit()
    }

    private fun logout() {
        model.logOut().observe(this, Observer{response->
            when(response.status){
                ApiStatus.SUCCESS -> {
                    CommonFuctions.dismissDialog()
                   // binding.progressBar.root.visibility= View.GONE

                    sharedpreferences.edit().clear().apply()
                    startActivity(Intent(this@HomeActivity, SignInActivity::class.java))
                    response.message?.let { it2-> showToast(applicationContext,it2) }
                    finish()
                }
                ApiStatus.ERROR -> {
                    CommonFuctions.dismissDialog()
                   // binding.progressBar.root.visibility=View.GONE
                    response.message?.let { it1-> showToast(applicationContext,it1) }
                }

                ApiStatus.LOADING ->
                {
                    CommonFuctions.showDialog(this)
                   // binding.progressBar.root.visibility=View.VISIBLE
                }
            }

        })
    }



}