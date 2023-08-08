package com.app.vibespace.viewModel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.vibespace.models.profile.BlockUserModel
import com.app.vibespace.models.profile.PostListModel
import com.app.vibespace.service.MyRepo
import com.app.vibespace.service.Resources
import com.app.vibespace.util.handleApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val repo: MyRepo,
    private val resources: android.content.res.Resources
):ViewModel() {

    fun getPostList(isSelf:Boolean?,post:String?)= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))

        try {
            val query: HashMap<String, Any> = hashMapOf()
            query["isSelf"] = isSelf ?: false
            query["post"] = post ?: ""

            val postResponse: PostListModel =repo.getPostList(query)
            if(postResponse.statusCode==200)
                emit(Resources.success(postResponse))
            else
                emit(Resources.error(postResponse.message,null))

        }catch(exe:Exception){
            handleApiError(exe,resources)
        }
    }

    fun blockUser(user:HashMap<String,String>)= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))

        try {
            val blockResponse:BlockUserModel=repo.blockUser(user)
            if(blockResponse.statusCode==200)
                emit(Resources.success(blockResponse))
            else
                emit(Resources.error(blockResponse.message,null))
        }catch(exe:Exception){
            handleApiError(exe,resources)
        }

    }


}