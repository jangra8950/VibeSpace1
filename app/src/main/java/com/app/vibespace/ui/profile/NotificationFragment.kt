package com.app.vibespace.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.R
import com.app.vibespace.databinding.FragmentNotificationBinding
import com.app.vibespace.viewModel.profile.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private lateinit var binding:FragmentNotificationBinding
    private val model:NotificationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if(!::binding.isInitialized)
            binding=FragmentNotificationBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPendingRequest()

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun getPendingRequest() {
       activity?.let{
           model.requestPending().observe(it){response->
               when(response.status){
                   ApiStatus.SUCCESS -> {
                       binding.tvCount.text= response?.data?.data?.totalCount.toString()
                   }
                   ApiStatus.ERROR -> {

                   }
                   ApiStatus.LOADING -> {

                   }
               }
           }
       }
    }
}