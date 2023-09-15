package com.app.vibespace.viewModel.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.vibespace.models.profile.BlockUserModel
import com.app.vibespace.models.profile.FollowModel
import com.app.vibespace.models.setting.FollowersModel
import com.app.vibespace.service.MyRepo
import com.app.vibespace.service.Resources
import com.app.vibespace.util.handleApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class FollowersViewModel @Inject constructor(
    private val repo: MyRepo,
    private val resources: android.content.res.Resources
):ViewModel(){

    fun getFollowers(value:String)= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))
        try {

            val query:HashMap<String,Any> = hashMapOf()
            query[value]=true

            val followerResponse:FollowersModel=repo.getFollowers(query)
            if(followerResponse.statusCode==200)
                emit(Resources.success(followerResponse))
            else
                emit(Resources.error(followerResponse.message,null))
        }catch (exe:Exception){
            handleApiError(exe,resources)
        }
    }

    fun unfollow(id:String)= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))
        try {
            val unfollow: FollowModel =repo.deleteUnfollow(id)
            if(unfollow.statusCode==200)
                emit(Resources.success(unfollow))
            else
                emit(Resources.error(unfollow.message,null))

        }catch (exe:Exception){
            emit(handleApiError(exe,resources))
        }
    }

    fun blockUser(id:String)= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))

        try {
            val query:HashMap<String,String> = hashMapOf()
            query["userId"] = id
            val blockResponse: BlockUserModel =repo.blockUser(query)
            if(blockResponse.statusCode==200)
                emit(Resources.success(blockResponse))
            else
                emit(Resources.error(blockResponse.message,null))
        }catch(exe:Exception){
            handleApiError(exe,resources)
        }

    }
}