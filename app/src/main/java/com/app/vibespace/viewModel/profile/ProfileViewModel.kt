package com.app.vibespace.viewModel.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.vibespace.models.profile.UserUpdateModel
import com.app.vibespace.service.MyRepo
import com.app.vibespace.service.Resources
import com.app.vibespace.util.handleApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo:MyRepo,
    private val resources: android.content.res.Resources
): ViewModel() {

  val ProfileName=MutableLiveData("")

    fun getProfile()= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))

       try {
           val profileResponse: UserUpdateModel =repo.getProfile()
           if(profileResponse.statusCode==200)
               emit(Resources.success(profileResponse))
           else
               emit(Resources.error(profileResponse.message,null))

       }catch (exe:Exception){
           emit(handleApiError(exe,resources))
       }
    }


}