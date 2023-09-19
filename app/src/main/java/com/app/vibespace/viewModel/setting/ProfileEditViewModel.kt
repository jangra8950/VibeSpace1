package com.app.vibespace.viewModel.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.vibespace.models.setting.UploadImageModel
import com.app.vibespace.service.MyRepo
import com.app.vibespace.service.Resources
import com.app.vibespace.util.MyApp.Companion.profileData
import com.app.vibespace.util.handleApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val repo: MyRepo,
    private val resources: android.content.res.Resources
):ViewModel(){

    fun uploadPicOnAws(file: MultipartBody.Part) = liveData(Dispatchers.IO) {
        emit(Resources.loading(null))

        try {
            val uploadResponse: UploadImageModel = repo.uploadImage(file)

            if (uploadResponse.statusCode == 200) {
                emit(Resources.success(uploadResponse))
                //profilePic.postValue(uploadResponse.data.url)
            } else {
                emit(Resources.error(uploadResponse.message, null))
            }

        } catch (exc: Exception) {
            emit(handleApiError(exc, resources))
        }
    }
}