package com.app.vibespace.viewModel.profile

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.vibespace.models.profile.UserUpdateModel
import com.app.vibespace.models.profile.UserUpdateRequest
import com.app.vibespace.models.registration.UniversityListModel
import com.app.vibespace.service.MyRepo
import com.app.vibespace.service.Resources
import com.app.vibespace.ui.profile.EditProfileFragmentArgs
import com.app.vibespace.util.handleApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val repo: MyRepo,
    private val resources: android.content.res.Resources
): ViewModel() {


    private var args:Bundle? = null
    var universityId:String?=null
    var university=MutableLiveData("")
    var sexuality=MutableLiveData("")
    var age=MutableLiveData("")
    var height=MutableLiveData("")
    var occuption=MutableLiveData("")

    fun setArguments(argumnent: Bundle?, Id: String){
        args=argumnent
        universityId=Id
        setValues()
    }

    private fun setValues(){
        args?.let{
            val arg=EditProfileFragmentArgs.fromBundle(it)
            university.value=arg.university
            sexuality.value=arg.sexuality
            age.value=arg.age
            height.value=arg.height
            occuption.value=arg.occupation


        }
    }

    fun getUniversityList()= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))

        try {
            val universityResponse: UniversityListModel =repo.getUniversityList()
            if(universityResponse.statusCode == 200){
                emit(Resources.success(universityResponse))
            }
            else
                emit(Resources.error(universityResponse.message,null))

        }catch (exe:Exception){
            emit(handleApiError(exe,resources))
        }
    }

    fun updateUser()= liveData(Dispatchers.IO) {
        emit(Resources.loading(null))

        try {

            val updateResponse:UserUpdateModel=repo.updateUser(addData())
            if(updateResponse.statusCode==200)
                emit(Resources.success(updateResponse))
            else
                emit(Resources.error(updateResponse.message,null))
        }catch(exe:Exception){
            handleApiError(exe,resources)
        }
    }

    private fun addData(): UserUpdateRequest {

         val obj=UserUpdateRequest()
        obj.gender=sexuality.value.toString()
        obj.height=height.value?.toDouble()
        obj.occupation=occuption.value.toString()
        obj.location?.lat="28.3949".toDouble()
        obj.location?.lng="84.1240".toDouble()
        return obj
    }

}