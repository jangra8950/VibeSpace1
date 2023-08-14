package com.app.vibespace.ui.registration



import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.app.vibespace.R
import com.app.vibespace.databinding.ActivitySignInBinding
import com.app.vibespace.models.registration.SignUpRequest
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {
    lateinit var binding:ActivitySignInBinding
    var data=SignUpRequest()
     var navHostFragment:NavHostFragment?=null
   // private var navController: NavController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = window.decorView
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
       if (!::binding.isInitialized)
           binding=ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment?

       if ( intent.getStringExtra("key")=="fromSettingActivity") {
           supportFragmentManager.beginTransaction()
               .replace(R.id.navHostFragment, ResetPasswordFragment())
               .commit()
       }


    }

    override fun onBackPressed() {
        if(navHostFragment!=null && navHostFragment!!.isAdded && navHostFragment!!.childFragmentManager.backStackEntryCount>0)
            navHostFragment!!.childFragmentManager.popBackStack()
        else
            super.onBackPressed()
    }
}



