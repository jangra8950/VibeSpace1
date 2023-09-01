package com.app.vibespace.ui.profile

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.R
import com.app.vibespace.databinding.FragmentCreatePostBinding
import com.app.vibespace.util.CommonFuctions

import com.app.vibespace.util.showToast
import com.app.vibespace.viewModel.profile.CreatePostViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatePostFragment : Fragment() {

    private lateinit var binding:FragmentCreatePostBinding
    private val model:CreatePostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(!::binding.isInitialized)
            binding=DataBindingUtil.inflate(inflater,R.layout.fragment_create_post,container,false)
        Log.i("SAHILDATAR","Post")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel=model
        binding.fragment=this
        binding.lifecycleOwner=this

        view.setOnClickListener { v ->
            binding.etText.clearFocus()
            val imm =
                requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("SAHILDATA","Post")
    }

    fun createPost(view:View){
       activity?.let {
           model.createPost().observe(it){response->
               when(response.status){
                   ApiStatus.SUCCESS -> {
                       CommonFuctions.dismissDialog()
                       model.caption.value=""
                       response.data?.data?.message?.let { it2-> showToast(requireActivity(),it2) }
                       findNavController().navigate(R.id.profileHostFragment)
                     // communicator.passData(Gson().toJson(response.data?.data))

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

}