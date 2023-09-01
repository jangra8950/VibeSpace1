package com.app.vibespace.service

import com.app.vibespace.models.profile.BlockUserModel
import com.app.vibespace.models.profile.ChatItemModel
import com.app.vibespace.models.profile.CreatePostModel
import com.app.vibespace.models.profile.CreatePostRequest
import com.app.vibespace.models.profile.DeleteAccountModel
import com.app.vibespace.models.profile.FollowModel
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
import com.app.vibespace.models.setting.UnblockUserModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiHelperImpl @Inject constructor(private val apiService:ApiRequest):ApiHelper {

    override suspend fun createOtp(params: CreateOtpRequest): CreateOtpModel =
        apiService.createOtp(params)

    override suspend fun verifyOtp(params: VerifyOtpRequest): VerifyOtpModel =
        apiService.verifyOtp(params)

    override suspend fun registerUser(params: SignUpRequest): UserUpdateModel =
        apiService.registerUser(params)

    override suspend fun getUniversityList(): UniversityListModel =
        apiService.getUniversityList()

    override suspend fun signInUser(params: SignInRequest): UserUpdateModel =
        apiService.signInUser(params)

    override suspend fun logOutUser(): LogOutModel =
        apiService.logoutUser()

    override suspend fun resetPassword(params: ResetPasswordRequest): ResetPasswordModel =
        apiService.resetPassword(params)

    override suspend fun getProfile(otherUserId: String): UserUpdateModel =
       apiService.getProfile(otherUserId)

    override suspend fun deleteAccount(): DeleteAccountModel =
        apiService.deleteAccount()

    override suspend fun updateUser(params: UserUpdateRequest): UserUpdateModel =
        apiService.updateUser(params)

    override suspend fun createPost(params: CreatePostRequest): CreatePostModel =
        apiService.createPost(params)

    override suspend fun getPostList(query: HashMap<String, Any>): PostListModel =
        apiService.getPostLList(query)

    override suspend fun deletePost(id: String): PostDeleteModel =
       apiService.deletePost(id)

    override suspend fun blockUser(params: HashMap<String, String>): BlockUserModel =
          apiService.blockUser(params)

    override suspend fun getBlockedUser(): BlockedUserListModel =
       apiService.getBlockedUser()

    override suspend fun unblockUser(id: String): UnblockUserModel =
       apiService.unblockUser(id)

    override suspend fun changePass(params: HashMap<String, String>): ResetPasswordModel =
      apiService.changePass(params)

    override suspend fun getUserList(): UserListModel =
      apiService.getUserList()

    override suspend fun postLIke(params:HashMap<String,String>): PostLikeCountModel =
      apiService.postLike(params)

    override suspend fun dislikePost(id: String): PostLikeCountModel =
      apiService.unlikePost(id)

    override suspend fun postReport(params: HashMap<String, String>): PostLikeCountModel =
       apiService.postReport(params)

    override suspend fun postComment(params: HashMap<String, String>): PostCommentModel =
       apiService.postComment(params)

    override suspend fun getCommentList(postId: String): PostCommentListModel =
       apiService.getCommentLList(postId)

    override suspend fun getPeople(params: HashMap<String, Any>): GetPeopleModel =
        apiService.getPeople(params)

    override suspend fun getSummary(): SummaryModel =
        apiService.getSummary()

    override suspend fun getChatInd(userId: String): ChatItemModel =
        apiService.getChatInd(userId)

    override suspend fun postFollow(params: HashMap<String, Any>): FollowModel =
       apiService.postFollow(params)

}