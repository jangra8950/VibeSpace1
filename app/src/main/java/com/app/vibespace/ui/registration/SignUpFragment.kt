package com.app.vibespace.ui.registration

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.vibespace.R
import com.app.vibespace.databinding.FragmentSignUpBinding
import com.app.vibespace.viewModel.registration.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

private lateinit var binding:FragmentSignUpBinding
 private val model:SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!::binding.isInitialized) {
            binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sign_up, container, false)

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel=model
        binding.fragment=this
        binding.lifecycleOwner = this

        view.setOnClickListener { v ->
            binding.etFirstName.clearFocus()
            binding.etLastName.clearFocus()
            val imm =
                requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }

    }

     fun onSignUp(view:View) {
         if(model.checkValue(view)) {
             (requireActivity()as SignInActivity).data.firstName= model.fName.value.toString()
             (requireActivity()as SignInActivity).data.lastName= model.lName.value.toString()
             findNavController().navigate(R.id.userDetailsFragment)
         }

    }
    fun onSignIn(view:View){
        findNavController().navigate(R.id.signInFragment)
    }


}