package com.app.vibespace.util

import android.app.Application
import android.app.SharedElementCallback
import android.content.Context
import android.content.SharedPreferences
import com.app.vibespace.models.profile.UserUpdateModel
import com.google.gson.Gson
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
    companion object{
        var profileData: UserUpdateModel.Data?=null

        lateinit var sharedpreferences: SharedPreferences
    }
    override fun onCreate() {
        super.onCreate()
        sharedpreferences = getSharedPreferences(ApiConstants.SHARED_PREFS, Context.MODE_PRIVATE)

        if (sharedpreferences.getString(ApiConstants.PROFILE_DATA,"")!=null && sharedpreferences.getString(ApiConstants.PROFILE_DATA,"")!="")
            profileData=Gson().fromJson(sharedpreferences.getString(ApiConstants.PROFILE_DATA,""),UserUpdateModel.Data::class.java)



    }
}