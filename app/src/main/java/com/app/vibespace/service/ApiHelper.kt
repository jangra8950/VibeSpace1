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


interface ApiHelper {

    suspend fun createOtp(params: CreateOtpRequest): CreateOtpModel

    suspend fun verifyOtp(params: VerifyOtpRequest): VerifyOtpModel

    suspend fun registerUser(params: SignUpRequest): UserUpdateModel

    suspend fun getUniversityList():UniversityListModel

    suspend fun signInUser(params:SignInRequest):UserUpdateModel

    suspend fun logOutUser():LogOutModel

    suspend fun resetPassword(params:ResetPasswordRequest):ResetPasswordModel

    suspend fun getProfile():UserUpdateModel

    suspend fun deleteAccount():DeleteAccountModel

    suspend fun updateUser(params:UserUpdateRequest):UserUpdateModel

}