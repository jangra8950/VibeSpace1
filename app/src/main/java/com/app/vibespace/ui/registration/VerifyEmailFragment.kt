package com.app.vibespace.ui.registration

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.Enums.CreateOtpType
import com.app.vibespace.R
import com.app.vibespace.databinding.FragmentVerifyEmailBinding
import com.app.vibespace.util.CommonFuctions


import com.app.vibespace.util.toast

import com.app.vibespace.viewModel.registration.VerifyEmailViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyEmailFragment : Fragment() {


 private lateinit var binding:FragmentVerifyEmailBinding
    private val model:VerifyEmailViewModel by viewModels()
    private val args:VerifyEmailFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!::binding.isInitialized) {
            binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_verify_email, container, false)

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel=model
        binding.fragment=this
        binding.lifecycleOwner = this

        view.setOnClickListener { v ->
            binding.etVerifyEmail.clearFocus()

            val imm =
                requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }

        arguments?.let{
            model.setArguments(arguments)
        }

    }


    fun onContinue(view:View){
        if(model.isValidEmail(requireContext())) {
             onCreateOtp(view)

        }
    }
    fun onSignIn(view:View){
        findNavController().navigate(R.id.signInFragment)
    }

    private fun onCreateOtp(view: View) {
        activity?.let {
            model.createOtp().observe(it) { response ->
                when (response.status) {

                    ApiStatus.ERROR -> {
                        CommonFuctions.dismissDialog()
                       // binding.progressBar.root.visibility=View.GONE
                        response.message?.let { it1 -> toast(view, it1) }
                    }

                    ApiStatus.SUCCESS -> {
                        CommonFuctions.dismissDialog()
                       // binding.progressBar.root.visibility=View.GONE
                        var otpValue:String?=null
                        if(args.otpType==CreateOtpType.UR.name)
                            otpValue=CreateOtpType.UR.name
                        if(args.otpType==CreateOtpType.UFP.name)
                            otpValue=CreateOtpType.UFP.name

                       val action=VerifyEmailFragmentDirections.actionVerifyEmailFragmentToVerifyOtpFragment(
                           email = model.email.value.toString().trim(),
                           otpType = otpValue.toString()
                       )
                        findNavController().navigate(action)

                    }

                    ApiStatus.LOADING -> {
                        CommonFuctions.showDialog(requireActivity())
                       // binding.progressBar.root.visibility=View.VISIBLE
                    }
                }
            }
        }
    }


}


