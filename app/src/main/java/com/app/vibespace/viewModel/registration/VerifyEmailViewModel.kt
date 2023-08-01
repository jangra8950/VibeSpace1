package com.app.vibespace.viewModel.registration

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.navigation.Navigation
import com.app.vibespace.Enums.CreateOtpType
import com.app.vibespace.R
import com.app.vibespace.models.registration.CreateOtpModel
import com.app.vibespace.models.registration.CreateOtpRequest
import com.app.vibespace.service.MyRepo
import com.app.vibespace.service.Resources
import com.app.vibespace.ui.registration.VerifyEmailFragmentArgs
import com.app.vibespace.util.handleApiError
import com.app.vibespace.util.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class VerifyEmailViewModel @Inject constructor(
    private val repo: MyRepo,
    private val resources: android.content.res.Resources
  ):ViewModel() {

    val email=MutableLiveData("")
    private var verifyEmailArgs: Bundle? = null
    private val pattern =
        "^[A-Za-z0-9._%+-]+@(tufts|bu|bc|harvard|mit|emerson|babson|umb|northeastern|simmons)\\.edu$"

    fun isValidEmail(context: Context):Boolean{
        if (email.value.toString().isEmpty()) {
            showToast(context, resources.getString(R.string.enter_email))

            return false
        }
        else if(!Pattern.compile(pattern).matcher(email.value.toString()).matches()){
            showToast(context,"Enter Valid Email for University Domain")
            return false
        }

        return true
    }






    fun createOtp() = liveData(Dispatchers.IO) {
        emit(Resources.loading(null))

        try {
            val otpResponse: CreateOtpModel = repo.createOtp(addCreateOtpData())

            if (otpResponse.statusCode == 200) {
                emit(Resources.success(otpResponse))
            }
            else {
                emit(Resources.error(otpResponse.message, null))
            }

        } catch (exc: Exception) {

            emit(handleApiError(exc, resources))
        }
    }

    private fun addCreateOtpData(): CreateOtpRequest {
        val otpRequest = CreateOtpRequest()
        otpRequest.email = email.value.toString().trim()

        verifyEmailArgs?.let{
            val args=VerifyEmailFragmentArgs.fromBundle(it)
            if(args.otpType==CreateOtpType.UR.name)
                otpRequest.type = CreateOtpType.UR.name
            if(args.otpType==CreateOtpType.UFP.name)
                otpRequest.type=CreateOtpType.UFP.name
        }

        return otpRequest
    }

     @SuppressLint("SuspiciousIndentation")
     fun onBack(view: View){
       val controller=Navigation.findNavController(view)
         controller.popBackStack()
    }

    fun setArguments(arguments: Bundle?) {
        this.verifyEmailArgs = arguments
    }

}