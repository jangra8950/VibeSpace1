package com.app.vibespace.ui.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels

import com.app.vibespace.R
import com.app.vibespace.databinding.FragmentHomeBinding
import com.app.vibespace.models.profile.UserUpdateModel
import com.app.vibespace.viewModel.registration.HomeViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    var data: UserUpdateModel?=null
    private lateinit var binding:FragmentHomeBinding
    private val model:HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
          if(!::binding.isInitialized){
              binding=DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
          }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel=model
        binding.fragment=this
        binding.lifecycleOwner=this
        getData()
    }

    private fun getData() {
       arguments?.let {
           val args=HomeFragmentArgs.fromBundle(it)
           binding.tvName.text=args.fName+" "+args.lName
          // model.setArguments(arguments)

       }

    }


}