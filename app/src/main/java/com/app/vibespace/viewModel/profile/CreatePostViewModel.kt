package com.app.vibespace.viewModel.profile

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.navigation.Navigation
import com.app.vibespace.models.profile.CreatePostModel
import com.app.vibespace.models.profile.CreatePostRequest
import com.app.vibespace.service.MyRepo
import com.app.vibespace.service.Resources
import com.app.vibespace.util.handleApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val repo: MyRepo,
    private val resources: android.content.res.Resources
):ViewModel() {

    val caption= MutableLiveData("")

    fun createPost()= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))
        try {

            val postResponse:CreatePostModel=repo.createPost(addData())
            if(postResponse.statusCode==200)
               emit(Resources.success(postResponse))
            else
                emit(Resources.error(postResponse.message,null))

        }catch(exe:Exception){
            handleApiError(exe,resources)
        }
    }

    private fun addData(): CreatePostRequest {
      val obj=CreatePostRequest()
        obj.postVisibility="day"
        obj.caption=caption.value.toString()
        obj.location?.lat="31".toDouble()
        obj.location?.lng="70".toDouble()
        return obj
    }

}