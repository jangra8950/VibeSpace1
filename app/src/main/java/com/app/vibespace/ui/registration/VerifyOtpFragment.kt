package com.app.vibespace.ui.registration

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.Enums.CreateOtpType
import com.app.vibespace.R
import com.app.vibespace.databinding.FragmentVerifyOtpBinding
import com.app.vibespace.databinding.LayoutProgressBarBinding
import com.app.vibespace.models.registration.VerifyOtpRequest
import com.app.vibespace.util.CommonFuctions


import com.app.vibespace.util.toast
import com.app.vibespace.viewModel.registration.OtpViewModel
import com.app.vibespace.viewModel.registration.TimerState
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyOtpFragment : Fragment() {

    private lateinit var binding:FragmentVerifyOtpBinding
    private val model: OtpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!::binding.isInitialized) {
            binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_verify_otp, container, false)

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel=model
        binding.fragment=this
        binding.lifecycleOwner = this
        view.setOnClickListener { v ->
            binding.etOtpOne.clearFocus()
            binding.etOtpTwo.clearFocus()
            binding.etOtpThree.clearFocus()
            binding.etOtpFour.clearFocus()
            val imm =
                requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }

        setup()

    }

    private fun setup() {
//        model.email.observe(viewLifecycleOwner){
//            binding.tvEmail.text=it
//        }

        arguments?.let {
           val safeArgs= VerifyOtpFragmentArgs.fromBundle(it)
            binding.tvEmail.text=safeArgs.email
            model.setArguments(arguments)

        }

      //  binding.tvEmail.text=args.email
        model.otpChange(binding)
        model.startTimer()
        timerObservers()

    }

    private fun timerObservers() {
        model.timerState.observe(viewLifecycleOwner) {
            when (it) {
                TimerState.Start -> {
                    binding.resendCode.visibility = View.GONE
                    binding.recieve.visibility = View.INVISIBLE
                    binding.tvTimer.visibility = View.VISIBLE
                }
                else -> {
                    binding.resendCode.visibility = View.VISIBLE
                    binding.recieve.visibility = View.VISIBLE
                    binding.tvTimer.visibility = View.GONE
                }
            }
        }

        model.timerValue.observe(viewLifecycleOwner) {
            binding.tvTimer.text = resources.getString(R.string.resend_code_time)
                .plus(" 00:").plus(it)
        }
    }


    fun onContinue(view:View){
        if(model.checkOtpValues(view))
            onVerifyOtp(view)

    }

    fun onResetOtpTap(view: View) {
        model.startTimer()
        timerObservers()
    }

    private fun onVerifyOtp(view: View) {
        activity?.let {
            model.verifyOtp().observe(it) { response ->
                when (response.status) {

                    ApiStatus.ERROR -> {
                        CommonFuctions.dismissDialog()
                        //binding.progressBar.root.visibility=View.GONE
                        response.message?.let { it1 -> toast(view, it1) }
                    }

                    ApiStatus.SUCCESS -> {
                        CommonFuctions.dismissDialog()
                       // binding.progressBar.root.visibility=View.GONE

                        arguments?.let { it ->
                            val safeArgs = VerifyOtpFragmentArgs.fromBundle(it)
                            (requireActivity()as SignInActivity).data.email=safeArgs.email
                            (requireActivity()as SignInActivity).data.otpToken=response.data?.data?.token
                            val action= VerifyOtpFragmentDirections.actionVerifyOtpFragmentToVerificationSuccessFragment(
                                type = response.data?.data?.type.toString()
                            )
                            findNavController().navigate(action)
                        }
                    }

                    ApiStatus.LOADING -> {
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