package com.app.vibespace.ui.profile

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.R
import com.app.vibespace.adapter.CommentListAdapter
import com.app.vibespace.databinding.FragmentFeedBinding
import com.app.vibespace.models.profile.PostListModel
import com.app.vibespace.adapter.PostAllAdapter
import com.app.vibespace.databinding.LayoutCommentListBinding
import com.app.vibespace.models.profile.PostCommentListModel
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
import java.util.Date

@AndroidEntryPoint
class FeedFragment : Fragment(),PostAllAdapter.Post {

    private lateinit var binding:FragmentFeedBinding
    private val model:FeedViewModel by viewModels()
    private var postList=ArrayList<PostListModel.Data.Post>()
    private var commentList=ArrayList<PostCommentListModel.Data.Comment>()
    lateinit var adapter: PostAllAdapter
    private var myMap = hashMapOf<String, String>()
   // var a: PostListModel.Data.Post?=null
    var check:Boolean?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(!::binding.isInitialized)
            binding=DataBindingUtil.inflate(inflater,R.layout.fragment_feed,container,false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel=model
        binding.fragment=this
        binding.lifecycleOwner=this

        binding.recyclerview.layoutManager= LinearLayoutManager(activity)
        adapter =  PostAllAdapter(postList,this,requireActivity())
        binding.recyclerview.adapter =  adapter

        getPostList()

        binding.ivSetting.setOnClickListener {
            startActivity(Intent(requireContext(),SettingActivity::class.java))
           // requireActivity().finish()
        }

      binding.ivSearchBar.setOnClickListener {

//          childFragmentManager.beginTransaction().replace(R.id.userListFragment, UserListProfileFragment())
//              .commit()

          (requireActivity() as HomeActivity).changeFragment(UserListProfileFragment())
      }


    }

    private fun getPostList() {
        activity?.let {
            model.getPostList(null,"all").observe(it){response->
                when(response.status){
                    ApiStatus.SUCCESS -> {

                        binding.shimmerLayout.startShimmer()
                        binding.shimmerLayout.visibility=View.GONE
                        binding.recyclerview.visibility=View.VISIBLE
                        // CommonFuctions.dismissDialog()
                        postList.clear()
                        postList.addAll(response?.data?.data!!.posts)

                        adapter.notifyDataSetChanged()
                    }
                    ApiStatus.ERROR -> {
                       //  CommonFuctions.dismissDialog()
                        response.message?.let { it1 -> showToast(requireActivity(), it1) }
                    }
                    ApiStatus.LOADING -> {
                       //  CommonFuctions.showDialog(requireActivity())
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

    private fun blockUser(position: Int, userId: String) {
        activity?.let {
            myMap["userId"] = userId
            model.blockUser(myMap).observe(it){response->
                when(response.status){
                    ApiStatus.SUCCESS -> {
                        response.data?.data?.message?.let { it1 -> showToast(requireActivity(), it1) }
                        getPostList()
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



    override fun block(position: Int, userId: String) {
        showDialogBlock(position,userId)
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
                      adapter.notifyItemChanged(position)
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
                      adapter.notifyItemChanged(position)

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
                          postList[position].isLiked=false
                          postList[position].likeCount=(postList[position].likeCount.toInt()-1).toString()
                          adapter.notifyItemChanged(position)
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
                         postList[position].isLiked=true
                         postList[position].likeCount=(postList[position].likeCount.toInt()+1).toString()
                         adapter.notifyItemChanged(position)
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

}