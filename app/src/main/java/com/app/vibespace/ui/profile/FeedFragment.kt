package com.app.vibespace.ui.profile

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
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.R
import com.app.vibespace.databinding.FragmentFeedBinding
import com.app.vibespace.models.profile.PostListModel
import com.app.vibespace.adapter.PostAdapter
import com.app.vibespace.adapter.PostAllAdapter
import com.app.vibespace.ui.settings.SettingActivity
import com.app.vibespace.util.CommonFuctions
import com.app.vibespace.util.showToast
import com.app.vibespace.viewModel.profile.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment : Fragment(),PostAllAdapter.Post {

    private lateinit var binding:FragmentFeedBinding
    private val model:FeedViewModel by viewModels()
    private var postList=ArrayList<PostListModel.Data.Post>()
    lateinit var adapter: PostAllAdapter
    private var myMap = hashMapOf<String, String>()
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
        adapter =  PostAllAdapter(postList,this)
        binding.recyclerview.adapter =  adapter

        getPostList(view)

        binding.ivSetting.setOnClickListener {
            startActivity(Intent(requireContext(),SettingActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun getPostList(view: View) {
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
                        postList.removeAt(position)
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

}