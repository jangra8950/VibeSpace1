package com.app.vibespace.ui.registration

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.Enums.CreateOtpType
import com.app.vibespace.R
import com.app.vibespace.databinding.FragmentResetPasswordBinding
import com.app.vibespace.models.registration.VerifyOtpModel
import com.app.vibespace.models.registration.VerifyOtpRequest
import com.app.vibespace.util.ApiConstants
import com.app.vibespace.util.CommonFuctions
import com.app.vibespace.util.MyApp
import com.app.vibespace.util.MyApp.Companion.profileData
import com.app.vibespace.util.showToast


import com.app.vibespace.util.toast
import com.app.vibespace.viewModel.registration.ResetPasswordViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordFragment : Fragment() {

  private lateinit var binding: FragmentResetPasswordBinding
  private val model:ResetPasswordViewModel by viewModels()
  private val args:ResetPasswordFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!::binding.isInitialized) {
            binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_reset_password, container, false)

        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel=model
        binding.fragment=this
        binding.lifecycleOwner = this

        view.setOnClickListener { v ->
            binding.etPass.clearFocus()
            binding.etConfirmPass.clearFocus()
            val imm =
                requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }

        setView()

    }

    private fun setView() {
        if(requireActivity().intent.getStringExtra("key")=="fromSettingActivity")
        {
            binding.tvOldPassword.visibility=View.VISIBLE
            binding.etOldPassLayout.visibility=View.VISIBLE
            binding.tvSetPass.text="Change Password"
            binding.btnSetPassword.text="UPDATE"
            binding.tvNewAccount.visibility=View.GONE
            binding.tvCreate.visibility=View.GONE

        }
    }

    private fun changePassword(view:View) {
       activity?.let{
           model.changePass().observe(it){response->
               when(response.status){
                   ApiStatus.SUCCESS -> {
                       CommonFuctions.dismissDialog()
                       response?.data?.data?.message?.let { it1 -> showToast(requireContext(), it1) }
                       onBack(view)
                   }
                   ApiStatus.ERROR -> {
                       CommonFuctions.dismissDialog()
                       response.message?.let { it1 -> showToast(requireContext(), it1) }
                   }
                   ApiStatus.LOADING -> {
                       CommonFuctions.showDialog(requireActivity())
                   }
               }
           }
       }
    }


    fun onSignIn(view:View){
        findNavController().navigate(R.id.signInFragment)
    }

    fun onContinue(view:View){

        var isFromSetting=false
        if(requireActivity().intent.getStringExtra("key")=="fromSettingActivity")
            isFromSetting=true
        if(model.checkValues(view,isFromSetting))
        {
            if(isFromSetting)
                changePassword(view)
            else if(args.type==CreateOtpType.UR.name)
               registerUser(view)
            else if(args.type==CreateOtpType.UFP.name)
                resetPassword(view)

        }

    }

    private fun registerUser(view:View) {
        activity?.let {
            model.registerUser(requireActivity()).observe(it) { response ->

                when (response.status) {

                    ApiStatus.ERROR -> {
                        CommonFuctions.dismissDialog()
                       // binding.progressBar.root.visibility=View.GONE
                        response.message?.let { it1 -> toast(view, it1) }
                    }
                    ApiStatus.SUCCESS -> {
                        CommonFuctions.dismissDialog()
                       // binding.progressBar.root.visibility=View.GONE
                        response.message?.let { it2-> toast(view,it2) }

                        val editor = MyApp.sharedpreferences.edit()
                        profileData=response.data?.data!!
                        editor.putString(ApiConstants.PROFILE_DATA, Gson().toJson(response.data?.data))
                        editor.apply()

                        val intent = Intent(requireContext(), HomeActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()

                    }

                    ApiStatus.LOADING -> {
                        CommonFuctions.showDialog(requireActivity())
                        //binding.progressBar.root.visibility=View.VISIBLE
                    }
                }
            }
        }
    }

    private fun resetPassword(view:View){
        activity?.let{
            model.resetPassword(requireActivity()).observe(it){response->
                when(response.status){
                    ApiStatus.ERROR -> {
                        CommonFuctions.dismissDialog()
                       // binding.progressBar.root.visibility=View.GONE
                        response.message?.let { it1 -> toast(view, it1) }
                    }

                    ApiStatus.SUCCESS -> {
                        CommonFuctions.dismissDialog()
                       // binding.progressBar.root.visibility=View.GONE
                        findNavController().navigate(R.id.signInFragment)
                        response.data?.data?.message?.let { it2-> toast(view,it2) }
                    }

                    ApiStatus.LOADING ->
                    {
                        CommonFuctions.showDialog(requireActivity())
                       //  binding.progressBar.root.visibility=View.VISIBLE
                    }
                }
            }
        }
    }
    fun onBack(view: View){
        requireActivity().onBackPressed()
    }

}