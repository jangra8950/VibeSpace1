package com.app.vibespace.service

import com.app.vibespace.models.profile.BlockUserModel
import com.app.vibespace.models.profile.ChatItemModel
import com.app.vibespace.models.profile.CreatePostModel
import com.app.vibespace.models.profile.CreatePostRequest
import com.app.vibespace.models.profile.DeleteAccountModel
import com.app.vibespace.models.profile.FollowModel
import com.app.vibespace.models.profile.MirrorPostModel
import com.app.vibespace.models.profile.PendingRequestModel
import com.app.vibespace.models.profile.PostCommentListModel
import com.app.vibespace.models.profile.PostCommentModel
import com.app.vibespace.models.profile.PostDeleteModel
import com.app.vibespace.models.profile.PostLikeCountModel
import com.app.vibespace.models.profile.PostListModel
import com.app.vibespace.models.profile.SummaryModel
import com.app.vibespace.models.profile.UserListModel
import com.app.vibespace.models.profile.UserUpdateModel
import com.app.vibespace.models.profile.UserUpdateRequest
import com.app.vibespace.models.registration.CreateOtpModel
import com.app.vibespace.models.registration.CreateOtpRequest
import com.app.vibespace.models.registration.GetPeopleModel
import com.app.vibespace.models.registration.LogOutModel
import com.app.vibespace.models.registration.ResetPasswordModel
import com.app.vibespace.models.registration.ResetPasswordRequest
import com.app.vibespace.models.registration.SignInRequest
import com.app.vibespace.models.registration.SignUpRequest
import com.app.vibespace.models.registration.UniversityListModel
import com.app.vibespace.models.registration.VerifyOtpModel
import com.app.vibespace.models.registration.VerifyOtpRequest
import com.app.vibespace.models.setting.BlockedUserListModel
import com.app.vibespace.models.setting.EditProfileModel
import com.app.vibespace.models.setting.FollowersModel
import com.app.vibespace.models.setting.UnblockUserModel
import com.app.vibespace.models.setting.UploadImageModel
import okhttp3.MultipartBody


interface ApiHelper {

    suspend fun createOtp(params: CreateOtpRequest): CreateOtpModel

    suspend fun verifyOtp(params: VerifyOtpRequest): VerifyOtpModel

    suspend fun registerUser(params: SignUpRequest): UserUpdateModel

    suspend fun getUniversityList():UniversityListModel

    suspend fun signInUser(params:SignInRequest):UserUpdateModel

    suspend fun logOutUser():LogOutModel

    suspend fun resetPassword(params:ResetPasswordRequest):ResetPasswordModel

    suspend fun getProfile(otherUserId:String):UserUpdateModel

    suspend fun deleteAccount():DeleteAccountModel

    suspend fun updateUser(params:UserUpdateRequest):UserUpdateModel

    suspend fun createPost(params:CreatePostRequest):CreatePostModel

    suspend fun getPostList(query: HashMap<String, Any>):PostListModel

    suspend fun deletePost(id:String):PostDeleteModel

    suspend fun blockUser(params:HashMap<String,String>):BlockUserModel

   suspend fun getBlockedUser():BlockedUserListModel

   suspend fun unblockUser(id:String):UnblockUserModel

   suspend fun changePass(params:HashMap<String,String>):ResetPasswordModel

   suspend fun getUserList():UserListModel

   suspend fun postLIke(params:HashMap<String,String>):PostLikeCountModel

   suspend fun dislikePost(id:String):PostLikeCountModel

    suspend fun postReport(params:HashMap<String,String>):PostLikeCountModel

    suspend fun postComment(params:HashMap<String,String>):PostCommentModel

    suspend fun getCommentList(postId:String):PostCommentListModel

    suspend fun getPeople(params:HashMap<String,Any>):GetPeopleModel

    suspend fun getSummary():SummaryModel

   suspend fun getChatInd(userId:String):ChatItemModel

   suspend fun postFollow(params:HashMap<String,Any>):FollowModel

   suspend fun deleteChat(param:HashMap<String,Any>):DeleteAccountModel

   suspend fun deleteUnfollow(id:String):FollowModel
   suspend fun mirrorPost(param:HashMap<String,String>):MirrorPostModel

   suspend fun getFollowers(params:HashMap<String,Any>):FollowersModel

    suspend fun uploadImage(file: MultipartBody.Part): UploadImageModel

    suspend fun editProfile(params:HashMap<String,Any>):EditProfileModel

   suspend fun getPendingRequest():PendingRequestModel
}