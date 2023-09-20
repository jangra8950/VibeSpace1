package com.app.vibespace.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.R
import com.app.vibespace.databinding.FragmentManageProfileBinding
import com.app.vibespace.ui.registration.SignInActivity
import com.app.vibespace.util.CommonFuctions
import com.app.vibespace.util.CommonFuctions.Companion.loadImage
import com.app.vibespace.util.CommonFuctions.Companion.showDialogConfirmation
import com.app.vibespace.util.MyApp
import com.app.vibespace.util.MyApp.Companion.profileData
import com.app.vibespace.util.showToast
import com.app.vibespace.viewModel.setting.ManageProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManageProfileFragment : Fragment() {

    private lateinit var binding:FragmentManageProfileBinding
    private val model:ManageProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if(!::binding.isInitialized)
            binding=FragmentManageProfileBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setValues()
        binding.btnLogOut.setOnClickListener {
            showDialogConfirmation(requireActivity(),"Are you sure you want to LogOut?") {
                logout()
            }
        }
        binding.btnDeleteAccount.setOnClickListener {
            showDialogConfirmation(requireActivity(),"Are you sure you want to Delete your Account?") {
                deleteAccount()
            }
        }
        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.ivSetting.setOnClickListener {
            findNavController().navigate(R.id.profileEditFragment)
        }
    }

    private fun logout() {
        activity?.let {
            model.logOut().observe(it){response->
                when(response.status){
                    ApiStatus.SUCCESS -> {
                        CommonFuctions.dismissDialog()

                        MyApp.sharedpreferences.edit().clear().apply()
                        startActivity(Intent(requireActivity(), SignInActivity::class.java))
                        response.data?.data?.message?.let { it2-> showToast(requireActivity(),it2) }
                        requireActivity().finish()
                    }
                    ApiStatus.ERROR -> {
                        CommonFuctions.dismissDialog()
                        response.message?.let { it1-> showToast(requireActivity(),it1) }
                    }

                    ApiStatus.LOADING ->
                    {
                        CommonFuctions.showDialog(requireActivity())

                    }
                }

            }
        }

    }

    private fun deleteAccount(){
        activity?.let {
            model.deleteAccount().observe(it){response->
                when(response.status){
                    ApiStatus.SUCCESS -> {
                        CommonFuctions.dismissDialog()

                        MyApp.sharedpreferences.edit().clear().apply()
                        startActivity(Intent(requireActivity(), SignInActivity::class.java))
                        response.data?.data?.message?.let { it2-> showToast(requireActivity(),it2) }
                        requireActivity().finish()
                    }
                    ApiStatus.ERROR ->{
                        CommonFuctions.dismissDialog()
                        response.message?.let { it1-> showToast(requireActivity(),it1) }
                    }
                    ApiStatus.LOADING ->{
                        CommonFuctions.showDialog(requireActivity())
                    }
                }

            }
        }
    }

    private fun setValues() {

        binding.tvName.text= profileData?.firstName
        binding.tvFollowersCount.text= profileData?.totalFollower.toString()
        binding.tvFollowingCount.text= profileData?.totalFollowing.toString()
        binding.tvCollegeName.text=profileData?.uniShortName?:"TUFTS"
        binding.tvProfileName.text= profileData?.bio.toString()
        profileData?.profilePic?.let { loadImage(requireActivity(), it,binding.ivAvatar) }

    }
}