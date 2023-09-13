package com.app.vibespace.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.app.vibespace.R
import com.app.vibespace.databinding.FragmentSettingsBinding
import com.app.vibespace.ui.registration.SignInActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding:FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(!::binding.isInitialized)
         //   binding=FragmentSettingsBinding.inflate(layoutInflater)
            binding=DataBindingUtil.inflate(inflater,R.layout.fragment_settings,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvBlocked.setOnClickListener {
            findNavController().navigate(R.id.blockedUserFragment)
        }

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.tvFollowings.setOnClickListener {
            findNavController().navigate(R.id.followersFragment)
        }

        binding.tvPassword.setOnClickListener {
            val intent = Intent(requireContext(), SignInActivity::class.java)
            intent.putExtra("key", "fromSettingActivity")
            startActivity(intent)
        }

    }

}