package com.app.vibespace.service

import com.app.vibespace.models.profile.DeleteAccountModel
import com.app.vibespace.models.profile.UserUpdateModel
import com.app.vibespace.models.profile.UserUpdateRequest
import com.app.vibespace.models.registration.CreateOtpModel
import com.app.vibespace.models.registration.CreateOtpRequest
import com.app.vibespace.models.registration.LogOutModel
import com.app.vibespace.models.registration.ResetPasswordModel
import com.app.vibespace.models.registration.ResetPasswordRequest
import com.app.vibespace.models.registration.SignInRequest
import com.app.vibespace.models.registration.SignUpRequest
import com.app.vibespace.models.registration.UniversityListModel
import com.app.vibespace.models.registration.VerifyOtpModel
import com.app.vibespace.models.registration.VerifyOtpRequest
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

    override suspend fun getProfile(): UserUpdateModel =
       apiService.getProfile()

    override suspend fun deleteAccount(): DeleteAccountModel =
        apiService.deleteAccount()

    override suspend fun updateUser(params: UserUpdateRequest): UserUpdateModel =
        apiService.updateUser(params)



}