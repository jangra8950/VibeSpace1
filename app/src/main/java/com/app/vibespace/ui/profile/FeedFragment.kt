package com.app.vibespace.ui.profile

import android.app.Activity
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
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.R
import com.app.vibespace.adapter.CommentListAdapter
import com.app.vibespace.databinding.FragmentFeedBinding
import com.app.vibespace.models.profile.PostListModel

import com.app.vibespace.databinding.LayoutCommentListBinding
import com.app.vibespace.models.profile.PostCommentListModel
import com.app.vibespace.paging.PostListPagingAdapter
import com.app.vibespace.ui.registration.HomeActivity
import com.app.vibespace.ui.settings.SettingActivity
import com.app.vibespace.util.CommonFuctions
import com.app.vibespace.util.CommonFuctions.Companion.dismissDialog
import com.app.vibespace.util.CommonFuctions.Companion.showDialog
import com.app.vibespace.util.MyApp.Companion.profileData
import com.app.vibespace.util.showToast
import com.app.vibespace.viewModel.profile.FeedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date

@AndroidEntryPoint
class FeedFragment : Fragment(),PostListPagingAdapter.Post {

    private lateinit var binding:FragmentFeedBinding
    private val model:FeedViewModel by viewModels()
    private var postList=ArrayList<PostListModel.Data.Post>()
    private var commentList=ArrayList<PostCommentListModel.Data.Comment>()
    lateinit var adap: PostListPagingAdapter
    private var myMap = hashMapOf<String, String>()
    private var selectedOption: String = "all"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if(!::binding.isInitialized)
            binding=DataBindingUtil.inflate(inflater,R.layout.fragment_feed,container,false)

        Log.i("SAHILDATAR","Feed")
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel=model
        binding.fragment=this
        binding.lifecycleOwner=this

        binding.recyclerview.layoutManager= LinearLayoutManager(activity)


        adap=PostListPagingAdapter(requireActivity(),this)
        binding.recyclerview.adapter=adap
        setData(selectedOption)

        binding.ivSetting.setOnClickListener {
            startActivity(Intent(requireContext(),NotificationActivity::class.java))
            //(requireActivity() as HomeActivity).changeFragment(NotificationFragment())
        }

        binding.ivSearchBar.setOnClickListener {
            startActivity(Intent(requireContext(),UserProfileActivity::class.java))
            //(requireActivity() as HomeActivity).changeFragment(UserListProfileFragment())
        }

        binding.tvAll.setOnClickListener {
            binding.tvFollowing.setTextColor(ContextCompat.getColor(it.context, R.color.colorEditTxt))
            binding.tvAll.setTextColor(ContextCompat.getColor(it.context, R.color.colorPrimary))
            selectedOption="all"
            setData(selectedOption)
        }
        binding.tvFollowing.setOnClickListener {
            binding.tvFollowing.setTextColor(ContextCompat.getColor(it.context, R.color.colorPrimary))
            binding.tvAll.setTextColor(ContextCompat.getColor(it.context, R.color.colorEditTxt))
            selectedOption="following"
            setData(selectedOption)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {

            if(selectedOption=="following")
                setData(selectedOption)
            else
                setData(selectedOption)
            binding.swipeRefreshLayout.isRefreshing=false
        }

    }

    private fun setData(data:String){
        adap=PostListPagingAdapter(requireActivity(),this)
        binding.recyclerview.adapter=adap
        getPostListPaging(data)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("SAHILDATA","Feed")
    }



    private fun blockUser(position: Int, userId: String) {
        activity?.let {
            myMap["userId"] = userId
            model.blockUser(myMap).observe(it){response->
                when(response.status){
                    ApiStatus.SUCCESS -> {
                        response.data?.data?.message?.let { it1 -> showToast(requireActivity(), it1) }
                        adap.notifyItemRemoved(position)
                        //adapter.notifyItemRemoved(position)
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



    override fun block(position: Int, userId: String) {
        blockUser(position,userId)
    }

    override fun like(postId: String,position: Int) {
        likePost(postId,position)
    }


    override fun report(postId: String, position: Int) {
        reportPost(postId,position)
    }

    private fun reportPost(postId: String, position: Int) {
        activity?.let{
            val query: HashMap<String, String> = hashMapOf()
            query["postId"] =postId
            model.postReport(query).observe(it){response->
                when(response.status){
                    ApiStatus.SUCCESS -> {
                        dismissDialog()
                        // adapter.notifyItemChanged(position)
                        response.data?.data?.message?.let { it1 -> showToast(requireActivity(), it1) }
                    }
                    ApiStatus.ERROR -> {
                        dismissDialog()
                        response.message?.let { it1 -> showToast(requireActivity(), it1) }
                    }
                    ApiStatus.LOADING -> {
                        showDialog(requireActivity())
                    }
                }
            }
        }
    }

    override fun unlike(postId: String, position: Int) {
        unlikePost(postId,position)
    }

    override fun comment(postId: String, position: Int, text: String) {
        commentPost(postId,position,text)
    }

    override fun commentList(postId: String, position: Int) {
        getCommentList(postId,position)
    }

    override fun vibe(caption: String, postVisibility: String) {
        mirrorPost(caption,postVisibility)
    }

    override fun chat(userId: String, image: String, name: String, mess: String) {
        startChatActivity(userId,image,name,mess)
    }
    private fun startChatActivity(userId: String, image: String, name: String,mess:String) {
        val intent = Intent(requireActivity(), ChatActivity::class.java)
        intent.putExtra("data", userId)
        intent.putExtra("name", name)
        intent.putExtra("image", image)
        intent.putExtra("mess", mess)
        startActivity(intent)
    }

    private fun mirrorPost(caption: String, postVisibility: String){
        activity?.let {
            model.mirrorPost(caption,postVisibility).observe(it){response->
                when(response.status){
                    ApiStatus.SUCCESS -> {
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

    private fun getCommentList(postId: String, position: Int) {
        activity?.let{
            model.postCommentList(postId).observe(it){response->
                when(response.status){
                    ApiStatus.SUCCESS -> {
                        dismissDialog()

                        commentList.clear()
                        commentList.addAll(response?.data?.data!!.comments)
                        showBottomSheet(postId,position,response?.data?.data!!.comments)
                        // showBottomSheet(postId,position)

//                      CommentAdapter.notifyItemChanged(position)

                    }
                    ApiStatus.ERROR -> {
                        dismissDialog()
                        response.message?.let { it1 -> showToast(requireActivity(), it1) }

                    }
                    ApiStatus.LOADING -> {
                        showDialog(requireActivity())
                    }
                }
            }
        }
    }

    private fun showBottomSheet(
        postId: String,
        position: Int, data: List<PostCommentListModel.Data.Comment>
    ) {
        var userData= PostCommentListModel.Data.Comment.UserDetails("",profileData?.firstName!!,profileData?.profilePic!!)
//        getCommentList(postId,position)
        val dialog = BottomSheetDialog(requireActivity())
        val view:LayoutCommentListBinding=LayoutCommentListBinding.inflate(layoutInflater)

        var commentAdapter= CommentListAdapter(data as ArrayList<PostCommentListModel.Data.Comment>,requireActivity())
        view.recyclerview.adapter=commentAdapter

        view.shimmerLayout.visibility=View.GONE
        view.recyclerview.visibility=View.VISIBLE

        view.ivSend.setOnClickListener {
            data.add(PostCommentListModel.Data.Comment("",Date().time.toInt(),false,"",null,view.tvTypeComment.text.toString(),userData,profileData?.userId!!))
            commentAdapter.notifyItemInserted(data.size-1)
            commentPost(postId,position,view.tvTypeComment.text.toString())
//            getCommentList(postId,position)
            view.tvTypeComment.clearFocus()
            val imm =
                context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.tvTypeComment.windowToken, 0)

            view.tvTypeComment.setText("")
        }



        dialog.setCancelable(true)
        dialog.setContentView(view.root)
        dialog.show()
    }

    private fun commentPost(postId: String, position: Int, text: String) {
        activity?.let {
            val query: HashMap<String, String> = hashMapOf()
            query["postId"] =postId
            query["text"]=text

            model.postComment(query).observe(it){response->
                when(response.status){
                    ApiStatus.SUCCESS -> {

                        response?.data?.data?.message?.let { it1 -> showToast(requireActivity(), it1) }
                        postList[position].commentCount=(postList[position].commentCount.toInt()+1).toString()

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
                        dismissDialog()
                        adap.likedClicked(position,false)
                    }
                    ApiStatus.ERROR -> {
                        dismissDialog()
                        response.message?.let { it1 -> showToast(requireActivity(), it1) }
                    }
                    ApiStatus.LOADING -> {
                        showDialog(requireActivity())
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
                        dismissDialog()
//                         var likeCount=(postList[position].likeCount.toInt()+1).toString()
                        adap.likedClicked(position,true)
                    }
                    ApiStatus.ERROR -> {
                        dismissDialog()
                        response.message?.let { it1 -> showToast(requireActivity(), it1) }
                    }
                    ApiStatus.LOADING -> {
                        showDialog(requireActivity())
                    }
                }
            }
        }
    }

    private fun getPostListPaging(value:String) {

        binding.shimmerLayout.startShimmer()
        binding.shimmerLayout.visibility=View.VISIBLE
        binding.recyclerview.visibility=View.GONE
        lifecycleScope.launch {

            model.getPostList(value).collectLatest { data ->

                if (adap.itemCount==0){
                    delay(1000)
                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.visibility=View.GONE
                    binding.recyclerview.visibility=View.VISIBLE
                }
                adap.submitData(data)
            }

        }
    }


}