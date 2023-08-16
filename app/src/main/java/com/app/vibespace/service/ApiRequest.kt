package com.app.vibespace.service

import com.app.vibespace.models.profile.BlockUserModel
import com.app.vibespace.models.profile.CreatePostModel
import com.app.vibespace.models.profile.CreatePostRequest
import com.app.vibespace.models.profile.DeleteAccountModel
import com.app.vibespace.models.profile.PostCommentListModel
import com.app.vibespace.models.profile.PostCommentModel
import com.app.vibespace.models.profile.PostDeleteModel
import com.app.vibespace.models.profile.PostLikeCountModel
import com.app.vibespace.models.profile.PostListModel
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
import com.app.vibespace.models.setting.UnblockUserModel
import com.app.vibespace.util.ApiConstants
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiRequest {

    @POST(ApiConstants.API_POST_CREATE_OTP)
    suspend fun createOtp(@Body params: CreateOtpRequest): CreateOtpModel

    @POST(ApiConstants.API_POST_VERIFY_OTP)
    suspend fun verifyOtp(@Body params: VerifyOtpRequest): VerifyOtpModel

    @POST(ApiConstants.API_POST_USER)
    suspend fun registerUser(@Body params: SignUpRequest):UserUpdateModel

    @GET(ApiConstants.API_GET_UNIVERSITY_LIST)
    suspend fun getUniversityList():UniversityListModel

    @POST(ApiConstants.API_POST_LOGIN_USER)
    suspend fun signInUser(@Body params: SignInRequest):UserUpdateModel

    @POST(ApiConstants.API_POST_LOGOUT)
    suspend fun logoutUser():LogOutModel

    @POST(ApiConstants.API_POST_RESET_PASSWORD)
    suspend fun resetPassword(@Body params:ResetPasswordRequest):ResetPasswordModel

    @GET(ApiConstants.API_GET_PROFILE)
    suspend fun getProfile():UserUpdateModel

    @DELETE(ApiConstants.API_DELETE_ACCOUNT)
    suspend fun deleteAccount():DeleteAccountModel

   @PUT(ApiConstants.API_UPDATE_USER)
   suspend fun updateUser(@Body params: UserUpdateRequest):UserUpdateModel

   @POST(ApiConstants.API_POST_CREATE_POST)
   suspend fun createPost(@Body params:CreatePostRequest):CreatePostModel

   @GET(ApiConstants.API_GET_POST_LIST)
   suspend fun getPostLList(@QueryMap query: HashMap<String,Any>):PostListModel
   //suspend fun getPostLList(@Query("isSelf") isSelf: Boolean?, @Query("post") post: String?):PostListModel

   @DELETE(ApiConstants.API_DELETE_POST)
   suspend fun deletePost(@Path("id") id: String):PostDeleteModel

   @POST(ApiConstants.API_POST_BLOCK_USER)
   suspend fun blockUser(@Body params:HashMap<String,String>):BlockUserModel

   @GET(ApiConstants.API_GET_BLOCKED_USER)
   suspend fun getBlockedUser():BlockedUserListModel

   @DELETE(ApiConstants.API_DELETE_UNBLOCK_USER)
   suspend fun unblockUser(@Path("id")id:String):UnblockUserModel

  @POST(ApiConstants.API_POST_CHANGE_PASS)
   suspend fun changePass(@Body params:HashMap<String,String>):ResetPasswordModel

   @GET(ApiConstants.API_GET_USER_LIST)
   suspend fun getUserList():UserListModel

   @POST(ApiConstants.API_POST_LIKE)
    suspend fun postLike(@Body params:HashMap<String,String>):PostLikeCountModel

    @DELETE(ApiConstants.API_DELETE_UNLIKE)
    suspend fun unlikePost(@Path("id")id:String):PostLikeCountModel

    @POST(ApiConstants.API_POST_REPORT)
    suspend fun postReport(@Body params:HashMap<String,String>):PostLikeCountModel

    @GET(ApiConstants.API_GET_COMMENT_LIST)
    suspend fun getCommentLList(@Query("postId") postId: String):PostCommentListModel

   @POST(ApiConstants.API_POST_COMMENT)
   suspend fun postComment(@Body params:HashMap<String,String>):PostCommentModel

   @GET(ApiConstants.API_GET_PEOPLE)
   suspend fun getPeople(@QueryMap query: HashMap<String,Any>):GetPeopleModel

}