package com.app.vibespace.viewModel.registration


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.navigation.Navigation
import com.app.vibespace.R
import com.app.vibespace.models.registration.UniversityListModel
import com.app.vibespace.service.MyRepo
import com.app.vibespace.service.Resources
import com.app.vibespace.util.handleApiError
import com.app.vibespace.util.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.time.Year
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val repo: MyRepo,
    private val resources: android.content.res.Resources
): ViewModel() {

    val university = MutableLiveData("")
    val dob=MutableLiveData("")
    val bio=MutableLiveData("")


    fun checkValue(view: View): Boolean {
        if (dob.value.toString().trim().isEmpty()) {
            showToast(view.context,resources.getString(R.string.please_select_dob_first))
            return false
        } else if (university.value.toString().trim().isEmpty()) {
            showToast(view.context,resources.getString(R.string.select_university_name))
            return false
        }else if (bio.value.toString().trim().isEmpty()) {
            showToast(view.context,resources.getString(R.string.write_your_bio))
            return false
        }

        return true
    }
    fun onBackTap(view: View) {
        val navController = Navigation.findNavController(view)
        navController.popBackStack()
    }


    fun datePicker(view:View){
        val c = Calendar.getInstance()
        c.add(Calendar.YEAR, -18);
        val years = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

           val datePickerDialog = DatePickerDialog(

               ContextThemeWrapper(view.context,R.style.CustomDatePicker),
               { _, year, monthOfYear, dayOfMonth ->

                   c.set(year,monthOfYear,dayOfMonth)
                   val myFormat = "dd/MM/yyyy"
                   val sdf = SimpleDateFormat(myFormat, Locale.US)
                  dob.value= sdf.format(c.time)
               },
               years, month, day
           )
        datePickerDialog.datePicker.maxDate = c.timeInMillis
//        datePickerDialog.datePicker.minDate = c2.timeInMillis

        datePickerDialog.show()

        val positiveColor = ContextCompat.getColor(view.context, R.color.colorPrimary)
        val negativeColor = ContextCompat.getColor(view.context, R.color.colorTxt)
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(negativeColor)
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(positiveColor)

    }

    fun getUniversityList()= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))

        try {
            val universityResponse:UniversityListModel=repo.getUniversityList()
            if(universityResponse.statusCode == 200){
                emit(Resources.success(universityResponse))
            }
            else
                emit(Resources.error(universityResponse.message,null))

        }catch (exe:Exception){
            emit(handleApiError(exe,resources))
        }
    }

}