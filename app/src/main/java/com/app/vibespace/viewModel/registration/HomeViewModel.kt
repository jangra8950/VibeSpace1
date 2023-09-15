package com.app.vibespace.viewModel.registration

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.vibespace.models.profile.UserUpdateModel
import com.app.vibespace.models.registration.GetPeopleModel
import com.app.vibespace.models.registration.LogOutModel
import com.app.vibespace.service.MyRepo
import com.app.vibespace.service.Resources

import com.app.vibespace.util.handleApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: MyRepo,
    private val resources: android.content.res.Resources

):ViewModel() {

    fun getPeople(type:String)= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))
        try {

            val query= hashMapOf<String,Any>()
            query["users"]=type
            query["lat"]=30.714
            query["lng"]=76.691

            val peopleResponse:GetPeopleModel=repo.getPeople(query)
            if(peopleResponse.statusCode==200)
                emit(Resources.success(peopleResponse))
            else
                emit(Resources.error(peopleResponse.message,null))

        }catch (exe:Exception){
            emit(handleApiError(exe,resources))
        }
    }

    fun getProfile(otherUserId:String)= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))

        try {
            val profileResponse: UserUpdateModel =repo.getProfile(otherUserId)
             if(profileResponse.statusCode==200)
                emit(Resources.success(profileResponse))
            else
                emit(Resources.error(profileResponse.message,null))

        }catch (exe:Exception){
            emit(handleApiError(exe,resources))
        }
    }


}