package com.app.vibespace.viewModel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.vibespace.models.setting.FollowersModel
import com.app.vibespace.service.MyRepo
import com.app.vibespace.service.Resources
import com.app.vibespace.util.handleApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class NewChatViewModel @Inject constructor(
    private val repo: MyRepo,
    private val resources: android.content.res.Resources
): ViewModel() {

    fun getFollowers()= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))
        try {

            val query:HashMap<String,Any> = hashMapOf()
            query["following"]=true

            val followerResponse: FollowersModel =repo.getFollowers(query)
            if(followerResponse.statusCode==200)
                emit(Resources.success(followerResponse))
            else
                emit(Resources.error(followerResponse.message,null))
        }catch (exe:Exception){
            handleApiError(exe,resources)
        }
    }
}