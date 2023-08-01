package com.app.vibespace.ui.registration


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.Enums.CreateOtpType
import com.app.vibespace.R
import com.app.vibespace.databinding.FragmentSignInBinding
import com.app.vibespace.util.ApiConstants.PROFILE_DATA
import com.app.vibespace.util.CommonFuctions.Companion.dismissDialog
import com.app.vibespace.util.CommonFuctions.Companion.showDialog
import com.app.vibespace.util.MyApp.Companion.profileData
import com.app.vibespace.util.MyApp.Companion.sharedpreferences
import com.app.vibespace.util.showToast
import com.app.vibespace.viewModel.registration.SignInViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignInFragment : Fragment() {


    private lateinit var binding:FragmentSignInBinding
    private val model:SignInViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (!::binding.isInitialized) {
            binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sign_in, container, false)

        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel=model
        binding.fragment=this
        binding.lifecycleOwner = this

        view.setOnClickListener { v ->
            binding.etEmail.clearFocus()
            binding.etPass.clearFocus()
            val imm =
                requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)

        }

    }



    fun forgot(view:View){

        val action=SignInFragmentDirections.actionSignInFragmentToVerifyEmailFragment(
            otpType = CreateOtpType.UFP.name
        )
        findNavController().navigate(action)
    }

    fun onSignUp(view:View){
        findNavController().navigate(R.id.signUpFragment)
    }

    fun onSignIn(view:View){
        if(model.checkValue(view))
           signInUser(view)
    }

    private fun signInUser(view:View) {

        activity?.let {
            model.signIn().observe(it){response->
                when(response.status){
                    ApiStatus.SUCCESS -> {

                        dismissDialog()
//                        binding.progressBar.root.visibility=View.GONE

                        val editor = sharedpreferences.edit()
                        profileData=response.data?.data!!
                        editor.putString(PROFILE_DATA, Gson().toJson(response.data?.data))
                        editor.apply()

                        startActivity(Intent(requireContext(), HomeActivity::class.java))
                        requireActivity().finish()

                    }
                    ApiStatus.ERROR -> {
                        dismissDialog()
                       // binding.progressBar.root.visibility=View.GONE
                        response.message?.let { it1-> showToast(requireContext(),it1) }
                    }

                    ApiStatus.LOADING -> {
                        showDialog(requireActivity())
                     //  binding.progressBar.root.visibility=View.VISIBLE

                    }
                }

            }
        }
    }




}