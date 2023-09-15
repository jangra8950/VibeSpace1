package com.app.vibespace.viewModel.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.models.profile.BlockUserModel
import com.app.vibespace.models.profile.MirrorPostModel
import com.app.vibespace.models.profile.PostCommentListModel
import com.app.vibespace.models.profile.PostCommentModel
import com.app.vibespace.models.profile.PostLikeCountModel
import com.app.vibespace.models.profile.PostListModel
import com.app.vibespace.paging.FeedPagingSource
import com.app.vibespace.service.MyRepo
import com.app.vibespace.service.Resources
import com.app.vibespace.util.handleApiError
import com.app.vibespace.util.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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

    fun postLike(user:HashMap<String,String>)= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))

        try{
            val postResponse:PostLikeCountModel=repo.postLike(user)
            if(postResponse.statusCode==200)
                emit(Resources.success(postResponse))
            else
                emit(Resources.error(postResponse.message,null))

        }catch(exe:Exception){
            handleApiError(exe,resources)
        }
    }

    fun postDislike(id:String)= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))

        try{
            val postResponse:PostLikeCountModel=repo.dislikePost(id)
            if(postResponse.statusCode==200)
                emit(Resources.success(postResponse))
            else
                emit(Resources.error(postResponse.message,null))

        }catch(exe:Exception){
            handleApiError(exe,resources)
        }
    }

    fun postReport(user:HashMap<String,String>)= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))

        try{
            val postResponse:PostLikeCountModel=repo.postReport(user)
            if(postResponse.statusCode==200)
                emit(Resources.success(postResponse))
            else
                emit(Resources.error(postResponse.message,null))

        }catch(exe:Exception){
            handleApiError(exe,resources)
        }
    }

    fun postComment(user:HashMap<String,String>)= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))

        try{
            val postResponse:PostCommentModel=repo.postComment(user)
            if(postResponse.statusCode==200)
                emit(Resources.success(postResponse))
            else
                emit(Resources.error(postResponse.message,null))

        }catch(exe:Exception){
            handleApiError(exe,resources)
        }
    }

    fun postCommentList(id:String)= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))

        try{
            val postResponse:PostCommentListModel=repo.getCommentList(id)
            if(postResponse.statusCode==200)
                emit(Resources.success(postResponse))
            else
                emit(Resources.error(postResponse.message,null))

        }catch(exe:Exception){
            handleApiError(exe,resources)
        }
    }

    fun mirrorPost(caption: String, postVisibility: String)= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))
        try{
            val query: HashMap<String, String> = hashMapOf()
            query["caption"]=caption
            query["postVisibility"]=postVisibility
            val response: MirrorPostModel =repo.mirrorPost(query)
            if(response.statusCode==200)
                emit(Resources.success(response))
            else
                emit(Resources.error(response.message,null))
        }catch(exe:Exception){
            handleApiError(exe,resources)
        }
    }

    fun getPostList(value:String) : Flow<PagingData<PostListModel.Data.Post>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true
            ),
            pagingSourceFactory =
            {
                FeedPagingSource(repo,value)
            }
        ).flow.cachedIn(viewModelScope)

    }


}