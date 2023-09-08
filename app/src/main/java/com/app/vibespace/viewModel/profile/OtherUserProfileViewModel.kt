package com.app.vibespace.viewModel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.vibespace.models.profile.BlockUserModel
import com.app.vibespace.models.profile.DeleteAccountModel
import com.app.vibespace.models.profile.FollowModel
import com.app.vibespace.models.profile.PostListModel
import com.app.vibespace.models.profile.UserUpdateModel
import com.app.vibespace.service.MyRepo
import com.app.vibespace.service.Resources
import com.app.vibespace.util.handleApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class OtherUserProfileViewModel @Inject constructor(
    private val repo: MyRepo,
    private val resources: android.content.res.Resources
):ViewModel() {

    fun getProfile(otherUserId:String)= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))

        try {
            val profileResponse: UserUpdateModel =repo.getProfile(otherUserId)

             if(profileResponse.statusCode==200)
                emit(Resources.success(profileResponse))
            else
                emit(Resources.error(profileResponse.message,null))

        }catch (exe:Exception){
            emit(handleApiError(exe,resources))
        }
    }

    fun getPostList(id:String?)= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))

        try {
            val query: HashMap<String, Any> = hashMapOf()
//            query["isSelf"] = isSelf ?: false
//            query["post"] = post ?: ""
            query["otherUserId"]=id ?:""
            val postResponse: PostListModel =repo.getPostList(query)
            if(postResponse.statusCode==200)
                emit(Resources.success(postResponse))
            else
                emit(Resources.error(postResponse.message,null))

        }catch(exe:Exception){
            handleApiError(exe,resources)
        }
    }

    fun postFollow(userId:String)= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))

        try {
            val query: HashMap<String, Any> = hashMapOf()
            query["userId"]=userId
            val followResponse: FollowModel =repo.postFollow(query)

            if(followResponse.statusCode==200)
                emit(Resources.success(followResponse))
            else
                emit(Resources.error(followResponse.message,null))

        }catch (exe:Exception){
            emit(handleApiError(exe,resources))
        }
    }

    fun postUnfollow(id:String)= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))
        try {
            val unfollow:FollowModel=repo.deleteUnfollow(id)
            if(unfollow.statusCode==200)
                emit(Resources.success(unfollow))
            else
                emit(Resources.error(unfollow.message,null))

        }catch (exe:Exception){
            emit(handleApiError(exe,resources))
        }
    }

    fun blockUser(user:HashMap<String,String>)= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))

        try {
            val blockResponse: BlockUserModel =repo.blockUser(user)
            if(blockResponse.statusCode==200)
                emit(Resources.success(blockResponse))
            else
                emit(Resources.error(blockResponse.message,null))
        }catch(exe:Exception){
            handleApiError(exe,resources)
        }

    }
}