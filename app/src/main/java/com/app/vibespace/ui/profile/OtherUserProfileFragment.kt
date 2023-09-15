package com.app.vibespace.ui.profile

import android.app.Dialog
import android.content.Intent
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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.R
import com.app.vibespace.adapter.OtherUserPostAdapter
import com.app.vibespace.databinding.FragmentOtherUserProfileBinding
import com.app.vibespace.models.profile.PostListModel
import com.app.vibespace.util.CommonFuctions
import com.app.vibespace.util.CommonFuctions.Companion.loadImage
import com.app.vibespace.util.showToast
import com.app.vibespace.viewModel.profile.OtherUserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtherUserProfileFragment : Fragment(),OtherUserPostAdapter.ChangesCallBack {

    private lateinit var binding:FragmentOtherUserProfileBinding
    private val args:OtherUserProfileFragmentArgs by navArgs()
    var userId=""
    private val model:OtherUserProfileViewModel by viewModels()
    private lateinit var adapter:OtherUserPostAdapter
    private var postList=ArrayList<PostListModel.Data.Post>()
    private var myMap = hashMapOf<String, String>()
    private  var isFollow:Boolean=false
    private var connectId=""
    private var textFollow="Follow"
    var userName=""
    var userImage=""

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


           if(arguments?.containsKey("user")==true)
               userId=arguments?.getString("user")?:""
           else
               userId=args.data

        binding.recyclerview.layoutManager= LinearLayoutManager(activity)
        adapter =  OtherUserPostAdapter(postList,requireActivity(),this)
        binding.recyclerview.adapter =  adapter

        getProfile(view,userId)
        getPostList(userId)

        binding.btnFollow.text=textFollow

        binding.btnFollow.setOnClickListener {
            if(isFollow)
                unfollow(view,connectId)
            else
                follow(view, userId)

        }


        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.btnMessage.setOnClickListener {
            startChatActivity(userId,userImage,userName,"")
        }

        Log.i("SASASA",userId)

    }

    private fun startChatActivity(userId: String, image: String, name: String,mess:String) {
        val intent = Intent(requireActivity(), ChatActivity::class.java)
        intent.putExtra("data", userId)
        intent.putExtra("name", name)
        intent.putExtra("image", image)
        intent.putExtra("mess", mess)
        startActivity(intent)
    }

    private fun follow(view: View, id: String) {
        activity?.let{
            model.postFollow(id).observe(it){response->
                when(response.status){
                    ApiStatus.SUCCESS ->{
                        response.data?.data?.message?.let { it1 -> showToast(requireActivity(), it1) }
                        binding.tvStreakCount.text=response?.data?.data?.response?.totalFollower.toString()
                        textFollow= "Following"
                        binding.btnFollow.text=textFollow
                        connectId=response?.data?.data?.response?.conncetId.toString()
                        isFollow=true
                    }

                    ApiStatus.ERROR -> {
                        response.message?.let { it1 -> showToast(requireActivity(), it1) }
                    }

                    ApiStatus.LOADING -> {}

                }
            }
        }
    }

    private fun unfollow(view: View, id: String) {
        activity?.let{
            model.postUnfollow(id).observe(it){response->
                when(response.status){
                    ApiStatus.SUCCESS ->{
                        response.data?.data?.message?.let { it1 -> showToast(requireActivity(), it1) }
                        binding.tvStreakCount.text=response?.data?.data?.response?.totalFollower.toString()
                        textFollow="Follow"
                        binding.btnFollow.text=textFollow
                        isFollow=false
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

    private fun getPostList( id: String) {
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
                        userName=response?.data?.data?.firstName.toString()+" "+response?.data?.data?.lastName.toString()
                        userImage=response?.data?.data?.profilePic!!
                        binding.tvProfileName.text= userName
                        binding.tvStreakCount.text= response.data.data.totalFollower.toString()
                        binding.tvVibesCount.text= response.data.data.totalFollowing.toString()
                        loadImage(requireActivity(),userImage,binding.ivAvatar)

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

    private fun blockUser(position: Int, userId: String) {
        activity?.let {
            myMap["userId"] = userId
            model.blockUser(myMap).observe(it){response->
                when(response.status){
                    ApiStatus.SUCCESS -> {

                        response.data?.data?.message?.let { it1 -> showToast(requireActivity(), it1) }
                        getPostList(userId)
                        //  postList.removeAt(position)
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

    private fun mirrorPost(caption: String, postVisibility: String){
      activity?.let {
          model.mirrorPost(caption,postVisibility).observe(it){response->
              when(response.status){
                  ApiStatus.SUCCESS -> {
                      Log.i("hdoasn",response.data?.data?.message!!)
                      response.data?.data?.message?.let { it1 -> showToast(requireActivity(), it1) }
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

    private fun unlikePost(postId: String, position: Int) {
        activity?.let{
            model.postDislike(postId).observe(it){response->
                when(response.status){
                    ApiStatus.SUCCESS -> {
                        CommonFuctions.dismissDialog()
                        postList[position].isLiked=false
                        postList[position].likeCount=(postList[position].likeCount.toInt()-1).toString()
                        adapter.notifyItemChanged(position)
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

    private fun likePost(postId: String,position: Int) {
        activity?.let {
            val query: HashMap<String, String> = hashMapOf()
            query["postId"] =postId
            model.postLike(query).observe(it){response->
                when(response.status){
                    ApiStatus.SUCCESS ->{
                        CommonFuctions.dismissDialog()
                        postList[position].isLiked=true
                        postList[position].likeCount=(postList[position].likeCount.toInt()+1).toString()
                        adapter.notifyItemChanged(position)
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

    override fun block(userId: String, position: Int) {
        blockUser(position,userId)
    }

    override fun chat(userId: String, image: String, name: String,mess:String) {
        startChatActivity(userId,image,name,mess)
    }

    override fun vibe(caption: String, postVisibility: String) {
        mirrorPost(caption,postVisibility)
    }

    override fun like(postId: String, position: Int) {
        likePost(postId,position)
    }

    override fun unlike(postId: String, position: Int) {
        unlikePost(postId,position)
    }
}