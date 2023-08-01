package com.app.vibespace.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.NavHostFragment
import com.app.vibespace.R

class ProfileHostFragment : Fragment() {

    private var navHostFragment: NavHostFragment?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_host_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navHostFragment =childFragmentManager.findFragmentById(R.id.navFragment) as NavHostFragment?
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity()) {
            if(navHostFragment!=null && navHostFragment!!.childFragmentManager.backStackEntryCount>0)
                navHostFragment!!.childFragmentManager.popBackStack()
            else
                requireActivity().finish()
        }
    }

}