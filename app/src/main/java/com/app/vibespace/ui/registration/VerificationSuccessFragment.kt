package com.app.vibespace.ui.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.vibespace.R
import com.app.vibespace.databinding.FragmentVerificationSuccessBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerificationSuccessFragment : Fragment() {

private lateinit var binding:FragmentVerificationSuccessBinding
private val args:VerificationSuccessFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!::binding.isInitialized) {
            binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_verification_success, container, false)

            navigate()
        }
        return binding.root
    }

    private fun navigate() {
      binding.back.setOnClickListener {
          findNavController().navigate(R.id.verifyOtpFragment)
      }

       binding.btnSuccess.setOnClickListener {
         val action=VerificationSuccessFragmentDirections.actionVerificationSuccessFragmentToResetPasswordFragment(
             type = args.type
         )
           findNavController().navigate(action)
       }

       binding.tvNewAccount.setOnClickListener {
           findNavController().navigate(R.id.signInFragment)
       }
    }


}