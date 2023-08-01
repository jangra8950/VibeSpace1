package com.app.vibespace.viewModel.registration

import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.vibespace.R
import com.app.vibespace.util.showToast
import com.app.vibespace.util.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val resources: android.content.res.Resources
):ViewModel() {

    val fName=MutableLiveData("")
    val lName=MutableLiveData("")


    fun checkValue(view: View): Boolean {
        if (fName.value.toString().trim().isEmpty()) {
            showToast(view.context,resources.getString(R.string.toast_firstName))
            return false
        } else if (lName.value.toString().trim().isEmpty()) {
           showToast(view.context,resources.getString(R.string.toast_lastName))
            return false
        }

        return true
    }





}