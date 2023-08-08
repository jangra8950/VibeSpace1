package com.app.vibespace.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.app.vibespace.R
import com.app.vibespace.databinding.FragmentBlockedUserBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlockedUserFragment : Fragment() {

   private lateinit var binding:FragmentBlockedUserBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(!::binding.isInitialized)
            binding=DataBindingUtil.inflate(inflater,R.layout.fragment_blocked_user,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}