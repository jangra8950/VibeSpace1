package com.app.vibespace.ui.settings

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.R
import com.app.vibespace.adapter.FollowersAdapter
import com.app.vibespace.adapter.UserListAdapter
import com.app.vibespace.databinding.FragmentFollowersBinding
import com.app.vibespace.databinding.LayoutFollowersListBinding
import com.app.vibespace.models.setting.FollowersModel
import com.app.vibespace.util.CommonFuctions
import com.app.vibespace.util.showToast
import com.app.vibespace.viewModel.setting.FollowersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowersFragment : Fragment(),FollowersAdapter.Follow {

    private lateinit var binding:FragmentFollowersBinding
    private lateinit var adapter:FollowersAdapter
    private val model:FollowersViewModel by viewModels()
    private var followerList=ArrayList<FollowersModel.Data.Follower>()
    private var allfollowerList=ArrayList<FollowersModel.Data.Follower>()
    private val args:FollowersFragmentArgs by navArgs()
    var followType="follower"
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
        adapter= FollowersAdapter(requireActivity(),this)
        binding.recyclerview.adapter=adapter


        if(args.value=="follower")
            follow()
        else
            following()

        getFollower(args.value)
        navigation()

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }
        searchFunctionality()
    }

    private fun navigation() {
        binding.tvFollowers.setOnClickListener {
            follow()
            followType="follower"
            getFollower("follower")
        }
        binding.tvFollowing.setOnClickListener {
            following()
            getFollower("following")
            followType="following"
        }
    }

    private fun following() {
        binding.tvHeading.text = "Following"
        binding.tvFollowers.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorEditTxt))
        binding.tvFollowing.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        binding.view1.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        binding.view2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorBackground))

    }

    private fun follow() {
        binding.tvHeading.text = "Followers"
        binding.tvFollowers.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        binding.tvFollowing.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorEditTxt))
        binding.view2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        binding.view1.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorBackground))
    }

    private fun searchFunctionality() {
        binding.etSearch.addTextChangedListener(
            afterTextChanged = {

            },
            onTextChanged = { s, start, before, count ->
                followerList.clear()
                if (!s.isNullOrEmpty())
                    followerList.addAll(allfollowerList.filter {
                        it.userDetails.firstName.contains(
                            s.toString(),
                            true
                        )
                    })
                else
                    followerList.addAll(allfollowerList)
                //adapter.notifyDataSetChanged()
                adapter.updateData(followerList,followType)
            },
            beforeTextChanged = { s, start, before, count ->

            }
        )
    }

    private fun getFollower(value: String) {
        activity?.let{
            model.getFollowers(value).observe(it){response->
                when(response.status){
                    ApiStatus.SUCCESS -> {
                        binding.shimmerLayout.startShimmer()
                        binding.shimmerLayout.visibility=View.GONE
                        binding.recyclerview.visibility=View.VISIBLE
                        followerList.clear()
                        allfollowerList.clear()
                        followerList.addAll(response?.data?.data!!.followers)
                        allfollowerList.addAll(response.data.data.followers)
                        adapter.updateData(followerList,value)


                    }
                    ApiStatus.ERROR ->{
                        response.message?.let { it1 -> showToast(requireActivity(), it1) }
                    }
                    ApiStatus.LOADING -> {

                    }
                }
            }
        }
    }

    private fun unFollowUser(id:String,position:Int){
        activity?.let {
            model.unfollow(id).observe(it){response->
                when(response.status){
                    ApiStatus.SUCCESS -> {
                        response.data?.data?.message?.let { it1 -> showToast(requireActivity(), it1) }
                        followerList.removeAt(position)
                        adapter.updateData(followerList,followType)
                        adapter.notifyItemRemoved(position)
                    }
                    ApiStatus.ERROR -> {

                    }
                    ApiStatus.LOADING -> {

                    }
                }
            }
        }
    }

    private fun blockUser(position: Int, userId: String) {
        activity?.let {

            model.blockUser(userId).observe(it){response->
                when(response.status){
                    ApiStatus.SUCCESS -> {

                        response.data?.data?.message?.let { it1 -> showToast(requireActivity(), it1) }
                        followerList.removeAt(position)
                        adapter.updateData(followerList,followType)
                        adapter.notifyItemRemoved(position)
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

    private fun showDialogBlock(position:Int, userId: String){
        CommonFuctions.dialog = Dialog(requireContext())
        CommonFuctions.dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        CommonFuctions.dialog?.setContentView(R.layout.layout_delete_confirm)
        CommonFuctions.dialog?.setCancelable(false)
        CommonFuctions.dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        CommonFuctions.dialog!!.findViewById<TextView>(R.id.btnYes).setOnClickListener {
            blockUser(position,userId)
            CommonFuctions.dialog!!.dismiss()
        }
        CommonFuctions.dialog!!.findViewById<TextView>(R.id.btnNo).setOnClickListener {
            CommonFuctions.dialog!!.dismiss()
        }
        CommonFuctions.dialog?.show()
    }
    override fun unfollow(id: String, position: Int) {
        unFollowUser(id,position)
    }

    override fun block(userId: String, position: Int) {
        showDialogBlock(position,userId)
    }

}