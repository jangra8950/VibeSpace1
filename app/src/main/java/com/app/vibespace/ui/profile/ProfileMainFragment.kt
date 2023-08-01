package com.app.vibespace.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.R
import com.app.vibespace.databinding.FragmentProfileMainBinding
import com.app.vibespace.models.profile.UserUpdateModel
import com.app.vibespace.util.ApiConstants
import com.app.vibespace.util.CommonFuctions
import com.app.vibespace.util.MyApp
import com.app.vibespace.util.MyApp.Companion.profileData
import com.app.vibespace.util.showToast
import com.app.vibespace.viewModel.profile.ProfileViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileMainFragment : Fragment() {

    private lateinit var binding:FragmentProfileMainBinding
    private val model:ProfileViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if(!::binding.isInitialized)
            binding=DataBindingUtil.inflate(inflater,R.layout.fragment_profile_main,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel=model
        binding.fragment=this
        binding.lifecycleOwner=this

        getProfile(view)
        navigation()
    }

    private fun navigation() {
        binding.btnBio.setOnClickListener {

//            val data = Gson().toJson(profileData)
//            val action =
//                ProfileMainFragmentDirections.actionProfileMainFragmentToUserProfileFragment(
//                    dataModel = data,
//                    sexuality = "",
//                    height = "",
//                    occupation = ""
//                )
//            findNavController().navigate(action)
            findNavController().navigate(R.id.userProfileFragment)
        }

        binding.btnMonetize.setOnClickListener {
            findNavController().navigate(R.id.priceProfileFragment)
        }


    }

    private fun getProfile(view:View) {
        activity?.let {
            model.getProfile().observe(it){response->
                when(response.status){
                    ApiStatus.SUCCESS -> {
                        CommonFuctions.dismissDialog()
                        val editor = MyApp.sharedpreferences.edit()
                        profileData?.insertDate= response.data?.data?.insertDate!!.toInt()
                        profileData?.totalFollower=response.data?.data?.totalFollower!!.toInt()
                        profileData?.totalFollowing= response.data.data.totalFollowing.toInt()
                        profileData?.gender=response.data.data.gender
                        profileData?.height=response.data.data.height
                        profileData?.occupation=response.data.data.occupation
                        profileData?.isPrivate=response.data.data.isPrivate
                        profileData?.universityName=response.data.data.universityName

                        editor.putString(ApiConstants.PROFILE_DATA, Gson().toJson(profileData))
                        editor.apply()
                        setValues(profileData!!)
                    }
                    ApiStatus.ERROR -> {
                        CommonFuctions.dismissDialog()
                        response.message?.let { it1 -> showToast(requireActivity(), it1) }
                    }
                    ApiStatus.LOADING -> {
                        CommonFuctions.showDialog(requireActivity())
                    }
                }
            }
        }
    }

    private fun setValues(data: UserUpdateModel.Data) {

        binding.tvProfileName.text=data.firstName +" "+data.lastName
        binding.tvFollowersCount.text=data.totalFollower.toString()
        binding.tvFollowingCount.text= data.totalFollowing.toString()
    }

}