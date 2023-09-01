package com.app.vibespace.viewModel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.vibespace.models.profile.ChatItemModel
import com.app.vibespace.service.MyRepo
import com.app.vibespace.service.Resources
import com.app.vibespace.util.handleApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ChatItemViewModel @Inject constructor(
    private val repo: MyRepo,
    private val resources: android.content.res.Resources
):ViewModel() {

   fun chat(userId:String)= liveData(Dispatchers.IO) {
       emit(Resources.loading(null))

       try {
           val chatResponse:ChatItemModel=repo.getChatInd(userId)
           if (chatResponse.statusCode==200)
               emit(Resources.success(chatResponse))
           else
               emit(Resources.error(chatResponse.message,null))

       }catch (exe:Exception){
           emit(handleApiError(exe,resources))
       }
   }
}