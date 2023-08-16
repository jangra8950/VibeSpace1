package com.app.vibespace.service

import com.app.vibespace.models.profile.BlockUserModel
import com.app.vibespace.models.profile.CreatePostRequest
import com.app.vibespace.models.profile.DeleteAccountModel
import com.app.vibespace.models.profile.PostCommentListModel
import com.app.vibespace.models.profile.PostCommentModel
import com.app.vibespace.models.profile.PostLikeCountModel
import com.app.vibespace.models.profile.UserListModel
import com.app.vibespace.models.profile.UserUpdateModel
import com.app.vibespace.models.profile.UserUpdateRequest
import com.app.vibespace.models.registration.CreateOtpRequest
import com.app.vibespace.models.registration.GetPeopleModel
import com.app.vibespace.models.registration.ResetPasswordModel
import com.app.vibespace.models.registration.ResetPasswordRequest
import com.app.vibespace.models.registration.SignInRequest
import com.app.vibespace.models.registration.SignUpRequest
import com.app.vibespace.models.registration.UniversityListModel
import com.app.vibespace.models.registration.VerifyOtpRequest
import com.app.vibespace.models.setting.BlockedUserListModel
import com.app.vibespace.models.setting.UnblockUserModel

import javax.inject.Inject


class MyRepo @Inject constructor(private val apiHelper:ApiHelper) {

    suspend fun createOtp(params: CreateOtpRequest) = apiHelper.createOtp(params)

    suspend fun verifyOtp(params: VerifyOtpRequest) = apiHelper.verifyOtp(params)

    suspend fun registerUser(params: SignUpRequest)= apiHelper.registerUser(params)

    suspend fun getUniversityList():UniversityListModel=apiHelper.getUniversityList()

    suspend fun signInUser(params:SignInRequest)=apiHelper.signInUser(params)

    suspend fun logOutUser()=apiHelper.logOutUser()

    suspend fun resetPassword(params:ResetPasswordRequest)=apiHelper.resetPassword(params)

    suspend fun getProfile(): UserUpdateModel =apiHelper.getProfile()

    suspend fun deleteAccount():DeleteAccountModel=apiHelper.deleteAccount()

    suspend fun updateUser(params:UserUpdateRequest)=apiHelper.updateUser(params)

    suspend fun createPost(params:CreatePostRequest)=apiHelper.createPost(params)

    suspend fun getPostList(query: HashMap<String,Any>)=apiHelper.getPostList(query)

    suspend fun deletePost(id:String)=apiHelper.deletePost(id)

    suspend fun blockUser(params:HashMap<String,String>):BlockUserModel=apiHelper.blockUser(params)

    suspend fun getBlockedUser():BlockedUserListModel=apiHelper.getBlockedUser()

    suspend fun unblockUser(id:String):UnblockUserModel=apiHelper.unblockUser(id)

    suspend fun changePass(params:HashMap<String,String>):ResetPasswordModel=apiHelper.changePass(params)

    suspend fun getUserList():UserListModel=apiHelper.getUserList()

    suspend fun postLike(params:HashMap<String,String>):PostLikeCountModel=apiHelper.postLIke(params)

    suspend fun dislikePost(id:String):PostLikeCountModel=apiHelper.dislikePost(id)

    suspend fun postReport(params:HashMap<String,String>):PostLikeCountModel=apiHelper.postReport(params)

    suspend fun postComment(params:HashMap<String,String>):PostCommentModel=apiHelper.postComment(params)

    suspend fun getCommentList(postId:String):PostCommentListModel=apiHelper.getCommentList(postId)

    suspend fun getPeople(params:HashMap<String,Any>):GetPeopleModel=apiHelper.getPeople(params)
}