package com.app.vibespace.viewModel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.vibespace.models.profile.SummaryModel
import com.app.vibespace.service.MyRepo
import com.app.vibespace.service.Resources
import com.app.vibespace.util.handleApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val repo: MyRepo,
    private val resources: android.content.res.Resources
):ViewModel() {

    fun getSummary()= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))
        try {
            val chatListResponse:SummaryModel=repo.getSummary()
            if(chatListResponse.statusCode==200)
                emit(Resources.success(chatListResponse))
            else
                emit(Resources.error(chatListResponse.message,null))

        }catch (exe:Exception){
            emit(handleApiError(exe,resources))
        }
    }
}