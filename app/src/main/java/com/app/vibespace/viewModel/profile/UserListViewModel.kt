package com.app.vibespace.viewModel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.vibespace.models.profile.UserListModel
import com.app.vibespace.service.MyRepo
import com.app.vibespace.service.Resources
import com.app.vibespace.util.handleApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val repo: MyRepo,
    private val resources: android.content.res.Resources
): ViewModel() {


    fun getUserList()= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))
        try {
            val listResponse:UserListModel=repo.getUserList()
            if(listResponse.statusCode==200)
              emit(Resources.success(listResponse))
            else
                emit(Resources.error(listResponse.message,null))
        }catch (exe:Exception){
            emit(handleApiError(exe,resources))
        }
    }
}