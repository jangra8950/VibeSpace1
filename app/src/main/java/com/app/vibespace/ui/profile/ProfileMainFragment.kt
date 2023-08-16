package com.app.vibespace.ui.profile

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.R
import com.app.vibespace.databinding.FragmentProfileMainBinding

import com.app.vibespace.models.profile.PostListModel
import com.app.vibespace.models.profile.UserUpdateModel
import com.app.vibespace.util.ApiConstants
import com.app.vibespace.util.CommonFuctions
import com.app.vibespace.util.MyApp
import com.app.vibespace.util.MyApp.Companion.profileData
import com.app.vibespace.adapter.PostAdapter
import com.app.vibespace.ui.registration.HomeActivity
import com.app.vibespace.ui.registration.SignInActivity
import com.app.vibespace.util.showToast
import com.app.vibespace.viewModel.profile.ProfileViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class ProfileMainFragment : Fragment(), PostAdapter.PostCallbacks {

    private lateinit var binding:FragmentProfileMainBinding
    private val model:ProfileViewModel by viewModels()
    private val args:ProfileMainFragmentArgs by navArgs()
    private var postList=ArrayList<PostListModel.Data.Post>()
    private var postCount by Delegates.notNull<Int>()
    lateinit var adapter: PostAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if(!::binding.isInitialized)
            binding=DataBindingUtil.inflate(inflater,R.layout.fragment_profile_main,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel=model
        binding.fragment=this
        binding.lifecycleOwner=this

        binding.recyclerview.layoutManager=LinearLayoutManager(activity)
        adapter =  PostAdapter(postList,this)
        binding.recyclerview.adapter =  adapter

        getProfile(view)
        getPostList(view)
        navigation()

    }

     private fun showDialogDelete(position:Int, postId: String){
        CommonFuctions.dialog = Dialog(requireContext())
        CommonFuctions.dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        CommonFuctions.dialog?.setContentView(R.layout.layout_delete_confirm)
        CommonFuctions.dialog?.setCancelable(false)
        CommonFuctions.dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        CommonFuctions.dialog!!.findViewById<TextView>(R.id.btnYes).setOnClickListener {
            deletePost(position,postId)
            CommonFuctions.dialog!!.dismiss()
        }
        CommonFuctions.dialog!!.findViewById<TextView>(R.id.btnNo).setOnClickListener {
            CommonFuctions.dialog!!.dismiss()
        }
        CommonFuctions.dialog?.show()
    }

    private fun deletePost(position: Int, postId: String) {
        activity?.let{
            model.deletePost(postId).observe(it){response->
                when(response.status){
                    ApiStatus.SUCCESS -> {
                        CommonFuctions.dismissDialog()
                        postList.removeAt(position)
                        adapter.notifyItemRemoved(position)
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


    private fun getPostList(view: View) {
       activity?.let {
           model.getPostList(true,null).observe(it){response->
               when(response.status){
                   ApiStatus.SUCCESS -> {

                       binding.shimmerLayout.startShimmer()
                       binding.shimmerLayout.visibility=View.GONE
                       binding.recyclerview.visibility=View.VISIBLE
                      // CommonFuctions.dismissDialog()
                       postList.clear()
                       postList.addAll(response?.data?.data!!.posts)
                       postCount= response.data.data.totalCount
                       adapter.notifyDataSetChanged()
                   }
                   ApiStatus.ERROR -> {
                      // CommonFuctions.dismissDialog()
                       response.message?.let { it1 -> showToast(requireActivity(), it1) }
                   }
                   ApiStatus.LOADING -> {
                      // CommonFuctions.showDialog(requireActivity())
                   }
               }
           }
       }

    }


    private fun getProfile(view:View) {
        activity?.let {
            model.getProfile().observe(it){response->
                when(response.status){
                    ApiStatus.SUCCESS -> {
                    //    CommonFuctions.dismissDialog()
                        val editor = MyApp.sharedpreferences.edit()
                        profileData?.insertDate= response.data?.data?.insertDate!!.toInt()
                        profileData?.totalFollower=response.data?.data?.totalFollower!!.toInt()
                        profileData?.totalFollowing= response.data.data.totalFollowing.toInt()
                        profileData?.gender=response.data.data.gender
                        profileData?.height=response.data.data.height
                        profileData?.occupation=response.data.data.occupation
                        profileData?.isPrivate=response.data.data.isPrivate
                        profileData?.universityName=response.data.data.universityName

                        editor.putString(ApiConstants.PROFILE_DATA, Gson().toJson(profileData))
                        editor.apply()
                        setValues(profileData!!)
                    }
                    ApiStatus.ERROR -> {

                        loginDialog()
                       //CommonFuctions.dismissDialog()
                      //  response.message?.let { it1 -> showToast(requireActivity(), it1) }
                    }
                    ApiStatus.LOADING -> {
                      //  CommonFuctions.showDialog(requireActivity())
                    }
                }
            }
        }
    }

    private fun loginDialog() {
        CommonFuctions.dialog = Dialog(requireContext())
        CommonFuctions.dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        CommonFuctions.dialog?.setContentView(R.layout.layout_access_token)
        CommonFuctions.dialog?.setCancelable(false)
        CommonFuctions.dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        CommonFuctions.dialog!!.findViewById<TextView>(R.id.btnYes).setOnClickListener {
            startActivity(Intent(requireContext(),SignInActivity::class.java))
            requireActivity().finish()
            CommonFuctions.dialog!!.dismiss()
        }

        CommonFuctions.dialog?.show()
    }

    private fun navigation() {
        binding.btnBio.setOnClickListener {
           // findNavController().navigate(R.id.userProfileFragment)
            (requireActivity() as HomeActivity).updateFragment(UserProfileFragment())
        }
//        binding.btnMonetize.setOnClickListener {
//            findNavController().navigate(R.id.priceProfileFragment)
//        }
    }

    private fun setValues(data: UserUpdateModel.Data) {

        binding.tvProfileName.text=data.firstName +" "+data.lastName
        binding.tvFollowersCount.text=data.totalFollower.toString()
        binding.tvFollowingCount.text= data.totalFollowing.toString()
    }

    override fun deletePostCallback(position: Int, postId: String) {
        showDialogDelete(position,postId)
    }


}