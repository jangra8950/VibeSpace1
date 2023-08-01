package com.app.vibespace.viewModel.registration

import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.vibespace.R
import com.app.vibespace.models.profile.UserUpdateModel
import com.app.vibespace.models.registration.SignInRequest
import com.app.vibespace.service.MyRepo
import com.app.vibespace.service.Resources
import com.app.vibespace.util.ApiConstants
import com.app.vibespace.util.handleApiError
import com.app.vibespace.util.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repo:MyRepo,
    private val resources: android.content.res.Resources
):ViewModel() {
    val email=MutableLiveData("")
    val password=MutableLiveData("")
    private val pattern =
        "^[A-Za-z0-9._%+-]+@(tufts|bu|bc|harvard|mit|emerson|babson|umb|northeastern|simmons)\\.edu$"

    fun checkValue(view: View): Boolean {
        if (email.value.toString().isEmpty() ||
            !Pattern.compile(pattern).matcher(email.value.toString()).matches()
        ) {
            showToast(view.context,resources.getString(R.string.please_enter_valid_email_address))

            return false
        } else if (password.value.toString().trim().isEmpty()) {
            showToast(view.context,resources.getString(R.string.please_enter_valid_password))
            return false
        } else if (password.value.toString().trim().length < 8) {
            showToast(view.context,resources.getString(R.string.password_must_be_8_character_long))

            return false
        }

        return true
    }

    fun signIn()= liveData(Dispatchers.IO){
        emit(Resources.loading(null))

        try{
            val signInResponse: UserUpdateModel =repo.signInUser(addSignInData())
            if(signInResponse.statusCode == 200)
                emit(Resources.success(signInResponse))
            else
                emit(Resources.error(signInResponse.message,null))

        }catch (exe:Exception){
            emit(handleApiError(exe,resources))
        }

    }

    private fun addSignInData(): SignInRequest {

        val request=SignInRequest()
        request.email=email.value.toString()
        request.password=password.value.toString()
        request.deviceToken=ApiConstants.Device_Token
        return request
    }


}