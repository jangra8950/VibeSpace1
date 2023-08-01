package com.app.vibespace.viewModel.registration

import android.app.Activity
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.navigation.Navigation
import com.app.vibespace.Enums.AuthType
import com.app.vibespace.Enums.DeviceType
import com.app.vibespace.R
import com.app.vibespace.models.profile.UserUpdateModel
import com.app.vibespace.models.registration.ResetPasswordModel
import com.app.vibespace.models.registration.ResetPasswordRequest
import com.app.vibespace.models.registration.SignUpRequest
import com.app.vibespace.service.MyRepo
import com.app.vibespace.service.Resources
import com.app.vibespace.ui.registration.SignInActivity
import com.app.vibespace.util.ApiConstants
import com.app.vibespace.util.handleApiError
import com.app.vibespace.util.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val repo: MyRepo,
    private val resources: android.content.res.Resources
):ViewModel() {

    val password=MutableLiveData("")
    val confirmPassword=MutableLiveData("")

    fun checkValues(view: View): Boolean {
        return when {
            password.value.toString().trim().isEmpty() -> {
                showToast(view.context,resources.getString(R.string.please_enter_valid_password))
                false
            }
            password.value.toString().trim().length < 8 -> {
                showToast(view.context,resources.getString(R.string.password_must_be_8_character_long))
                false
            }
            confirmPassword.value.toString().trim().isEmpty() -> {
                showToast(view.context,resources.getString(R.string.please_enter_valid_confirm_password))
                false
            }
            confirmPassword.value.toString().trim().length < 8 -> {
                showToast(view.context,resources.getString(R.string.confirm_password_must_be_8_character_long))
                false
            }
            password.value.toString().trim() != confirmPassword.value.toString().trim() -> {
                showToast(view.context,resources.getString(R.string.password_and_confirm_password_should_match_each_other))
                false
            }
            else -> {
                true
            }
        }
    }

    fun registerUser(mActivity: Activity)= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))

        try {
            (mActivity as SignInActivity).data.password=password.value.toString()
            (mActivity as SignInActivity).data.confirmPassword=confirmPassword.value.toString()
            (mActivity as SignInActivity).data.deviceType = DeviceType.IOS.toString().lowercase(Locale.getDefault())
            (mActivity as SignInActivity).data.authType= AuthType.APP.toString().lowercase(Locale.getDefault())
            (mActivity as SignInActivity).data.deviceToken=ApiConstants.Device_Token
            (mActivity as SignInActivity).data.profilePic=ApiConstants.Profile_pic

            val userResponse: UserUpdateModel =repo.registerUser(mActivity.data)
            if(userResponse.statusCode == 200){
                emit(Resources.success(userResponse))
            }
            else{
                emit(Resources.error(userResponse.message,null))
            }

        }catch(exe:Exception){
            emit(handleApiError(exe,resources))
        }
    }

    fun resetPassword(mActivity: Activity)= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))

        try {
           val userResponse:ResetPasswordModel=repo.resetPassword(setData(mActivity))
            if(userResponse.statusCode == 200){
                emit(Resources.success(userResponse))
            }
            else{
                emit(Resources.error(userResponse.message,null))
            }

        }catch(exe:Exception){
            emit(handleApiError(exe,resources))
        }
    }

    private fun setData(mActivity: Activity): ResetPasswordRequest {
      val request=ResetPasswordRequest()
        request.newPassword=password.value.toString()
        request.confirmPassword=confirmPassword.value.toString()
        request.email= (mActivity as SignInActivity).data.email.toString()
        request.otpToken= (mActivity).data.otpToken!!.toInt()
        return request
    }


    fun onBack(view: View){
        val controller= Navigation.findNavController(view)
        controller.popBackStack()
    }
}