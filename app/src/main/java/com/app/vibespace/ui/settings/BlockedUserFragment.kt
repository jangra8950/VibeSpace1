package com.app.vibespace.ui.settings

import android.app.Dialog
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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.R
import com.app.vibespace.adapter.BlockedUserAdapter
import com.app.vibespace.adapter.PostAllAdapter
import com.app.vibespace.databinding.FragmentBlockedUserBinding
import com.app.vibespace.models.setting.BlockedUserListModel
import com.app.vibespace.util.CommonFuctions
import com.app.vibespace.util.showToast
import com.app.vibespace.viewModel.setting.BlockedUserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlockedUserFragment : Fragment(),BlockedUserAdapter.Unblock {

   private lateinit var binding:FragmentBlockedUserBinding
   private val model:BlockedUserViewModel by viewModels()
   private var blockedList=ArrayList<BlockedUserListModel.Data.BlockUser>()
   private lateinit var adapter:BlockedUserAdapter
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

        binding.recyclerview.layoutManager= LinearLayoutManager(activity)
        adapter =  BlockedUserAdapter(blockedList,this)
        binding.recyclerview.adapter =  adapter

       getBlockedUser(view)

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun getBlockedUser(view: View) {
       activity?.let{
           model.getBlockedUserList().observe(it){response->
               when(response.status){
                   ApiStatus.SUCCESS -> {
                       binding.shimmerLayout.startShimmer()
                       binding.shimmerLayout.visibility=View.GONE
                       binding.recyclerview.visibility=View.VISIBLE

                       blockedList.clear()
                       blockedList.addAll(response?.data?.data!!.blockUserList)
                       adapter.notifyDataSetChanged()

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
            unblockUser(position,userId)
            CommonFuctions.dialog!!.dismiss()
        }
        CommonFuctions.dialog!!.findViewById<TextView>(R.id.btnNo).setOnClickListener {
            CommonFuctions.dialog!!.dismiss()
        }
        CommonFuctions.dialog?.show()
    }

    private fun unblockUser(position: Int, userId: String) {
        activity?.let{
            model.unblockUser(userId).observe(it){response->
                when(response.status){
                    ApiStatus.SUCCESS -> {
                        response.data?.data?.message?.let { it1 -> showToast(requireActivity(), it1) }
                        blockedList.removeAt(position)
                        adapter.notifyItemRemoved(position)
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

    override fun unblock(position: Int, userId: String) {
        showDialogBlock(position,userId)
    }

}