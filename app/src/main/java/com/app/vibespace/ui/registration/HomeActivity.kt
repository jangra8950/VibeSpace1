package com.app.vibespace.ui.registration


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.app.vibespace.R
import com.app.vibespace.databinding.ActivityHomeBinding


import com.app.vibespace.util.MyApp.Companion.profileData
import com.app.vibespace.viewModel.registration.HomeViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    lateinit var navHostFragment:NavHostFragment
    lateinit var navController:NavController
    private var doubleBackToExitPressedOnce=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = window.decorView
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        binding=DataBindingUtil.setContentView(this,R.layout.activity_home)
        Log.i("SAHIL_DATA",Gson().toJson(profileData))


       navHostFragment = supportFragmentManager.findFragmentById(R.id.flFragment) as NavHostFragment
       navController = navHostFragment.navController
     //  NavigationUI.setupWithNavController(binding.navigation, navController)


        binding.navigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.homeFragment->{
                    if (navController.currentDestination?.id!=R.id.homeFragment) {
                        findNavController(R.id.flFragment).navigate(R.id.homeFragment)
                        supportFragmentManager.popBackStack(
                            null,
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )
                        true
                    }
                    else
                        false
                }
                R.id.feedFragment->{
                   findNavController(R.id.flFragment).navigate(R.id.feedFragment)
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    true}
                R.id.profileHostFragment->{

                    findNavController(R.id.flFragment).navigate(R.id.profileHostFragment)
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    true}
                R.id.chatFragment->{
                    findNavController(R.id.flFragment).navigate(R.id.chatFragment)
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

                    true}
                R.id.createPostFragment->{
                    findNavController(R.id.flFragment).navigate(R.id.createPostFragment)
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

                    true}
                else->false
            }

        }
    }


    override fun onBackPressed() {

        if (supportFragmentManager.backStackEntryCount>0)
            supportFragmentManager.popBackStack()
        else if(navHostFragment!=null && navHostFragment!!.isAdded && navHostFragment!!.childFragmentManager.backStackEntryCount>0)
            navHostFragment!!.childFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }

            doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                doubleBackToExitPressedOnce = false
            }, 2000)
        }
    }

    fun updateFragment(frag:Fragment){
        supportFragmentManager.beginTransaction().add(R.id.flFragment, frag).addToBackStack(null)
            .commit()
    }
    fun changeFragment(frag:Fragment){
        supportFragmentManager.beginTransaction().add(R.id.userListFragment, frag).addToBackStack(null)
            .commit()
    }

}