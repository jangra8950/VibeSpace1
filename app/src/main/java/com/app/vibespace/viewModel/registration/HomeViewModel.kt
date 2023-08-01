package com.app.vibespace.viewModel.registration

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.vibespace.models.registration.LogOutModel
import com.app.vibespace.service.MyRepo
import com.app.vibespace.service.Resources
import com.app.vibespace.ui.registration.HomeFragmentArgs
import com.app.vibespace.util.handleApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: MyRepo,
    private val resources: android.content.res.Resources
):ViewModel() {



    @SuppressLint("SuspiciousIndentation")
    fun logOut()= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))
        try{
            val logOutResponse:LogOutModel=repo.logOutUser()
               if(logOutResponse.statusCode==200)
                  emit(Resources.success(logOutResponse))
               else
                   emit(Resources.error(logOutResponse.message,null))
        }catch (exe:Exception){
            emit(handleApiError(exe,resources))
        }
    }


}