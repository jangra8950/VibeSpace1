package com.app.vibespace.viewModel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.vibespace.models.profile.DeleteAccountModel
import com.app.vibespace.models.registration.LogOutModel
import com.app.vibespace.service.MyRepo
import com.app.vibespace.service.Resources
import com.app.vibespace.util.handleApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val repo:MyRepo,
    private val resources: android.content.res.Resources
): ViewModel() {

    fun logOut()= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))
        try{
            val logOutResponse: LogOutModel =repo.logOutUser()
            if(logOutResponse.statusCode==200)
                emit(Resources.success(logOutResponse))
            else
                emit(Resources.error(logOutResponse.message,null))
        }catch (exe:Exception){
            emit(handleApiError(exe,resources))
        }
    }

    fun deleteAccount()= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))
        try{
            val deleteResponse:DeleteAccountModel=repo.deleteAccount()
            if(deleteResponse.statusCode==200)
                emit(Resources.success(deleteResponse))
            else
                emit(Resources.error(deleteResponse.message,null))

        }catch(exe:Exception){
            handleApiError(exe,resources)
        }
    }

}