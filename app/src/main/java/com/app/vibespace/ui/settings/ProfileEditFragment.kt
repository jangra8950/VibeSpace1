package com.app.vibespace.ui.settings

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.R

import com.app.vibespace.databinding.FragmentProfileEditBinding
import com.app.vibespace.databinding.LayoutGalleryDialogBinding
import com.app.vibespace.util.ApiConstants
import com.app.vibespace.util.CommonFuctions.Companion.loadImage
import com.app.vibespace.util.MyApp
import com.app.vibespace.util.MyApp.Companion.profileData
import com.app.vibespace.util.MyApp.Companion.sharedpreferences
import com.app.vibespace.util.showToast
import com.app.vibespace.viewModel.setting.ProfileEditViewModel

import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.intuit.sdp.BuildConfig
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class ProfileEditFragment : Fragment() {

    private lateinit var binding:FragmentProfileEditBinding
    private val model:ProfileEditViewModel by viewModels()
    private var latestTmpUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         if(!::binding.isInitialized)
             binding=FragmentProfileEditBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivCamera.setOnClickListener {
            gallaryDialog()
        }

    }

    private val pickMedia=registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
        if(it!=null)
            startCrop(it)
    }

    private val takePhotoContract=registerForActivityResult(ActivityResultContracts.TakePicture()){
        if(it)
            startCrop(latestTmpUri!!)                               // take and crop
    }

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            val uriFilePath = result.getUriFilePath(requireActivity())
            val file = File(uriFilePath.toString())
              val avatarBody = file.asRequestBody("file".toMediaTypeOrNull())
             val multipartBody = MultipartBody.Part.createFormData("file", file.name, avatarBody)
//            val part=MultipartBody.Part.createFormData("file",file.name,
//                RequestBody.create("image/*".toMediaTypeOrNull(), file))

            uploadImage(multipartBody)
        }
    }

    private fun startCrop(imageUri: Uri?) {
        cropImage.launch(
            options {
                setGuidelines(CropImageView.Guidelines.ON)
            }
        )
    }


    private fun gallaryDialog() {
        val dialog= BottomSheetDialog(requireActivity())
        val view=LayoutGalleryDialogBinding.inflate(LayoutInflater.from(requireActivity()))
        dialog.setCancelable(false)
        dialog.setContentView(view.root)
        view.tvGallary.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            dialog.cancel()
        }
        view.tvCamera.setOnClickListener{
            takePhoto()
            dialog.cancel()
        }
        dialog.show()
    }

    private fun takePhoto() {
        lifecycleScope.launchWhenStarted {
            getTempFileUri().let{uri->
                latestTmpUri=uri
                takePhotoContract.launch(uri)
            }
        }
    }

    private fun getTempFileUri():Uri {

        val tmpFile = File.createTempFile("tmp_image_file", ".png").apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.provider", tmpFile)
    }

    private fun uploadImage(file:MultipartBody.Part){
        activity?.let{
            model.uploadPicOnAws(file).observe(it){response->
                when(response.status){
                    ApiStatus.SUCCESS -> {
                        val editor = sharedpreferences.edit()
                        profileData?.profilePic=response.data?.data?.url.toString()
                        editor.putString(ApiConstants.PROFILE_DATA, Gson().toJson(profileData))
                        editor.apply()
                        loadImage(requireActivity(), profileData?.profilePic.toString(),binding.ivProfileMain)
                    }
                    ApiStatus.ERROR -> {
                        response.data?.message?.let { it1 -> showToast(requireActivity(), it1) }
                    }
                    ApiStatus.LOADING -> {

                    }
                }

            }
        }
    }
}