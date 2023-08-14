package com.app.vibespace.viewModel.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.vibespace.models.setting.BlockedUserListModel
import com.app.vibespace.models.setting.UnblockUserModel
import com.app.vibespace.service.MyRepo
import com.app.vibespace.service.Resources
import com.app.vibespace.util.handleApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class BlockedUserViewModel @Inject constructor(
    private val repo: MyRepo,
    private val resources: android.content.res.Resources
): ViewModel() {

    fun getBlockedUserList()= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))

        try {
            val listResponse:BlockedUserListModel=repo.getBlockedUser()
            if(listResponse.statusCode==200)
                emit(Resources.success(listResponse))
            else
                emit(Resources.error(listResponse.message,null))

        }catch (exe:Exception){
            handleApiError(exe,resources)
        }
    }

    fun unblockUser(id:String)= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))

        try {

            val unblockResponse:UnblockUserModel=repo.unblockUser(id)
            if(unblockResponse.statusCode==200)
                emit(Resources.success(unblockResponse))
            else
                emit(Resources.error(unblockResponse.message,null))

        }catch (exe:Exception){
            handleApiError(exe,resources)
        }
    }
}