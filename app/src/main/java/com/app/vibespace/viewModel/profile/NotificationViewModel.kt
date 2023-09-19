package com.app.vibespace.viewModel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.vibespace.models.profile.PendingRequestModel
import com.app.vibespace.service.MyRepo
import com.app.vibespace.service.Resources
import com.app.vibespace.util.handleApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repo: MyRepo,
    private val resources: android.content.res.Resources
):ViewModel() {

    fun requestPending()= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))
        try {
           val requestReaponse:PendingRequestModel=repo.getPendingRequest()
           if(requestReaponse.statusCode==200)
               emit(Resources.success(requestReaponse))
           else
               emit(Resources.error(requestReaponse.message,null))
        }catch(exe:Exception){
            handleApiError(exe,resources)
        }
    }
}