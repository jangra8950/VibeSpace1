package com.app.vibespace.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.R
import com.app.vibespace.adapter.OtherUserPostAdapter
import com.app.vibespace.adapter.PostAdapter
import com.app.vibespace.databinding.FragmentOtherUserProfileBinding
import com.app.vibespace.models.profile.PostListModel
import com.app.vibespace.util.showToast
import com.app.vibespace.viewModel.profile.OtherUserProfileViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtherUserProfileFragment : Fragment() {

    private lateinit var binding:FragmentOtherUserProfileBinding
    private val args:OtherUserProfileFragmentArgs by navArgs()
    private val model:OtherUserProfileViewModel by viewModels()
    private lateinit var adapter:OtherUserPostAdapter
    private var postList=ArrayList<PostListModel.Data.Post>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if(!::binding.isInitialized)
            binding=DataBindingUtil.inflate(inflater,R.layout.fragment_other_user_profile,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel=model
        binding.fragment=this
        binding.lifecycleOwner=this
        var a= arrayOf("")

        binding.recyclerview.layoutManager= LinearLayoutManager(activity)
        adapter =  OtherUserPostAdapter(postList)
        binding.recyclerview.adapter =  adapter

        binding.btnFollow.setOnClickListener {
            follow(view,args.data)
        }

        Log.i("SASASA",args.data)
        getProfile(view,args.data)
        getPostList(view,args.data)
    }

    private fun follow(view: View, id: String) {
        activity?.let{
            model.postFollow(id).observe(it){response->
                when(response.status){
                    ApiStatus.SUCCESS ->{
                        response.data?.data?.message?.let { it1 -> showToast(requireActivity(), it1) }
                        binding.tvStreakCount.text=response?.data?.data?.response?.totalFollower.toString()
                    }

                    ApiStatus.ERROR -> {
                        response.message?.let { it1 -> showToast(requireActivity(), it1) }
                    }

                    ApiStatus.LOADING -> {}

                }
            }
        }
    }

    private fun getPostList(view: View, id: String) {
        activity?.let{
            model.getPostList(id).observe(it){response->
               when(response.status){
                   ApiStatus.SUCCESS -> {
                       binding.shimmerLayout.startShimmer()
                       binding.shimmerLayout.visibility=View.GONE
                       binding.recyclerview.visibility=View.VISIBLE

                       postList.clear()
                       postList.addAll(response?.data?.data!!.posts)
                       adapter.notifyDataSetChanged()
                   }
                   ApiStatus.ERROR -> {
                       response.message?.let { it1 -> showToast(requireActivity(), it1) }

                   }
                   ApiStatus.LOADING ->{

                   }
               }
            }
        }
    }

    private fun getProfile(view: View, id: String) {
        activity?.let{
            model.getProfile(id).observe(it){response->
                when(response.status){
                    ApiStatus.SUCCESS -> {
                        binding.tvProfileName.text= response?.data?.data?.firstName.toString()+" "+response?.data?.data?.lastName.toString()
                        binding.tvStreakCount.text=response?.data?.data?.totalFollower.toString()
                        binding.tvVibesCount.text= response?.data?.data?.totalFollowing.toString()

                    }
                    ApiStatus.ERROR -> {
                        response.message?.let { it1 -> showToast(requireActivity(), it1) }
                    }
                    ApiStatus.LOADING -> {

                    }
                }
            }
        }
    }
}