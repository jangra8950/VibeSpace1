package com.app.vibespace.viewModel.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.vibespace.models.profile.PostDeleteModel
import com.app.vibespace.models.profile.PostListModel
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

   // val ProfileName=MutableLiveData("")

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

    fun getPostList(isSelf:Boolean?,post:String?)= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))

        try {
            val query: HashMap<String, Any> = hashMapOf()
            query["isSelf"] = isSelf ?: false
            query["post"] = post ?: ""

            val postResponse:PostListModel=repo.getPostList(query)
            if(postResponse.statusCode==200)
                emit(Resources.success(postResponse))
            else
                emit(Resources.error(postResponse.message,null))

        }catch(exe:Exception){
            handleApiError(exe,resources)
        }
    }

    fun deletePost(id:String)= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))

        try {
            val deleteResponse:PostDeleteModel=repo.deletePost(id)
            if(deleteResponse.statusCode==200)
                emit(Resources.success(deleteResponse))
            else
                emit(Resources.error(deleteResponse.message,null))

        }catch(exe:Exception){
            handleApiError(exe,resources)
        }

    }

}