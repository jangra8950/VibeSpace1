package com.app.vibespace.ui.registration

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
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
import com.app.vibespace.ui.profile.ChatFragment
import com.app.vibespace.ui.profile.CreatePostFragment
import com.app.vibespace.ui.profile.FeedFragment
import com.app.vibespace.ui.profile.ProfileHostFragment
import com.app.vibespace.ui.profile.ProfileMainFragment
import com.app.vibespace.ui.profile.UserListProfileFragment
import com.app.vibespace.util.CommonFuctions
import com.app.vibespace.util.Communicator
import com.app.vibespace.util.MyApp
import com.app.vibespace.util.MyApp.Companion.profileData
import com.app.vibespace.util.MyApp.Companion.sharedpreferences

import com.app.vibespace.util.showToast
import com.app.vibespace.util.toast

import com.app.vibespace.viewModel.registration.HomeViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() , Communicator{
    private lateinit var binding: ActivityHomeBinding
    var postData=""
    private val model:HomeViewModel by viewModels()
    private var doubleBackToExitPressedOnce=false
   // private var navHostFragment: NavHostFragment?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = window.decorView
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        binding=DataBindingUtil.setContentView(this,R.layout.activity_home)
        Log.i("SAHIL_DATA",Gson().toJson(profileData))

        setFragment()

    }

    private fun setFragment() {
        val bundle = Bundle()
        if (postData!="") {
            bundle.putString("message", postData)
            postData=""
        }
        setCurrentFragment(HomeFragment())


        binding.navigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.map->{
                    setCurrentFragment(HomeFragment())
                    true}
                R.id.feed->{setCurrentFragment(FeedFragment())
                    true}
                R.id.profile->{setCurrentFragment(ProfileHostFragment())
                    true}
                R.id.chat->{setCurrentFragment(ChatFragment())
                    true}
                R.id.post->{setCurrentFragment(CreatePostFragment())
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

    override fun passData(edit: String) {
        val bundle = Bundle()
        bundle.putString("message", edit)
        postData=edit
        val transaction = supportFragmentManager.beginTransaction()

        // Created instance of fragment2
        val fragment2 =ProfileMainFragment()
        binding.navigation.selectedItemId = R.id.profile;
//        fragment2.arguments = bundle
//        transaction.replace(R.id.flFragment,fragment2)
//        transaction.commit()
    }

    override fun onBackPressed() {

        if (supportFragmentManager.backStackEntryCount>0)
            supportFragmentManager.popBackStack()
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