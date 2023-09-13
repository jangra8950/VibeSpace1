package com.app.vibespace.ui.settings

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.vibespace.R
import com.app.vibespace.adapter.FollowersAdapter
import com.app.vibespace.adapter.UserListAdapter
import com.app.vibespace.databinding.FragmentFollowersBinding
import com.app.vibespace.viewModel.setting.FollowersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowersFragment : Fragment() {

    private lateinit var binding:FragmentFollowersBinding
    private lateinit var adapter:FollowersAdapter
    private val model:FollowersViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        if(!::binding.isInitialized)
            binding=FragmentFollowersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerview.layoutManager= LinearLayoutManager(activity)
        adapter= FollowersAdapter()
        binding.recyclerview.adapter=adapter

        Log.i("jnw","clnanc")

    }

}