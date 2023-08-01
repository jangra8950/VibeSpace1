package com.app.vibespace.viewModel.registration

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.app.vibespace.Enums.CreateOtpType
import com.app.vibespace.R
import com.app.vibespace.databinding.FragmentVerifyOtpBinding
import com.app.vibespace.models.registration.VerifyOtpModel
import com.app.vibespace.models.registration.VerifyOtpRequest
import com.app.vibespace.service.MyRepo
import com.app.vibespace.service.Resources
import com.app.vibespace.ui.registration.VerifyOtpFragmentArgs
import com.app.vibespace.util.ApiConstants
import com.app.vibespace.util.handleApiError
import com.app.vibespace.util.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(
    private val repo: MyRepo,
    private val resources: android.content.res.Resources
):ViewModel() {


    val otp1 = MutableLiveData("")
    val otp2 = MutableLiveData("")
    val otp3 = MutableLiveData("")
    val otp4 = MutableLiveData("")

    private var verifyOtpArgs: Bundle? = null

    private val _timerState: MutableLiveData<TimerState> = MutableLiveData(TimerState.Start)
    val timerState: LiveData<TimerState>
        get() = _timerState

    private val _timerValue: MutableLiveData<String> = MutableLiveData("")
    val timerValue: LiveData<String>
        get() = _timerValue

    private var cTimer: CountDownTimer? = null

    fun checkOtpValues(view: View): Boolean {
        val otp = otp1.value.toString().trim() + otp2.value.toString().trim() +
                otp3.value.toString().trim() + otp4.value.toString().trim()

        if (otp.isEmpty()) {
            showToast(view.context,resources.getString(R.string.enter_otp))
            return false
        } else if (otp.length != 4) {
            showToast(view.context,resources.getString(R.string.enter_valid_otp))
            return false
        }

        return true
    }

    private fun cancelTimer() {
        cTimer?.cancel()
    }

    fun startTimer() {
        _timerState.postValue(TimerState.Start)

        cTimer = object : CountDownTimer(ApiConstants.Timer_Value.toLong(), 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {

                val timerValue = if ((millisUntilFinished / 1000).toString().length == 1) {
                    "0".plus(millisUntilFinished / 1000)
                } else {
                    (millisUntilFinished / 1000).toString()
                }
                _timerValue.postValue(timerValue)
            }

            override fun onFinish() {
                //  _timerState.value=TimerState.Finished
                _timerState.postValue(TimerState.Finished)
                cancelTimer()
            }
        }
        (cTimer as CountDownTimer).start()
    }


    fun otpChange(binding:FragmentVerifyOtpBinding){
        binding.etOtpOne.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(char: CharSequence?, start: Int, before: Int, count: Int) {
                 if (char?.length == 1) {
                    binding.etOtpTwo.requestFocus()
                }
            }
        })

        binding.etOtpTwo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(char: CharSequence?, start: Int, before: Int, count: Int) {
                if (char?.length == 0) {
                    binding.etOtpOne.requestFocus()
                } else if (char?.length == 1) {
                    binding.etOtpThree.requestFocus()
                }
            }
        })

        binding.etOtpThree.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(char: CharSequence?, start: Int, before: Int, count: Int) {
                if (char?.length == 0) {
                    binding.etOtpTwo.requestFocus()
                } else if (char?.length == 1) {
                    binding.etOtpFour.requestFocus()
                }
            }
        })

        binding.etOtpFour.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(char: CharSequence?, start: Int, before: Int, count: Int) {
                if (char?.length == 0) {
                    binding.etOtpThree.requestFocus()
                }
            }
        })
    }

    fun onBack(view: View){
        cancelTimer()
        val navController = Navigation.findNavController(view)
        navController.popBackStack()
    }

    fun verifyOtp() = liveData(Dispatchers.IO) {
        emit(Resources.loading(null))

        try {
            val verifyResponse: VerifyOtpModel =
                repo.verifyOtp(addVerifyOtpData())

            if (verifyResponse.statusCode == 200) {
                emit(Resources.success(verifyResponse))
            } else {
                emit(Resources.error(verifyResponse.message, null))
            }

        } catch (exc: Exception) {

            emit(handleApiError(exc, resources))
        }
    }

    private fun addVerifyOtpData(): VerifyOtpRequest {
        val otp = otp1.value.toString().trim() + otp2.value.toString().trim() +
                otp3.value.toString().trim() + otp4.value.toString().trim()

        val verifyRequest = VerifyOtpRequest()

        verifyOtpArgs?.let {
            val safeArgs = VerifyOtpFragmentArgs.fromBundle(it)

            verifyRequest.email = safeArgs.email
            verifyRequest.otp = otp.toInt()

            verifyRequest.type = safeArgs.otpType
        }

        return verifyRequest
    }

    fun setArguments(arguments: Bundle?) {
        this.verifyOtpArgs = arguments
    }
}
sealed class TimerState {
    object Start : TimerState()
    object Finished : TimerState()
}