package com.app.vibespace.ui.profile

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.R
import com.app.vibespace.adapter.ChatListAdapter

import com.app.vibespace.databinding.FragmentChatBinding
import com.app.vibespace.models.profile.SummaryModel
import com.app.vibespace.util.showToast
import com.app.vibespace.viewModel.profile.ChatListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment(), ChatListAdapter.Summary {

    private lateinit var binding:FragmentChatBinding
    private lateinit var adapter: ChatListAdapter
    private val model:ChatListViewModel by viewModels()
    private var chatList=ArrayList<SummaryModel.Data.ChatSummary>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(!::binding.isInitialized)
            binding=DataBindingUtil.inflate(inflater,R.layout.fragment_chat,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSummary()

        binding.recyclerview.layoutManager= LinearLayoutManager(activity)
        adapter= ChatListAdapter(chatList,requireContext(),this)
        binding.recyclerview.adapter=adapter


    }

    override fun onResume() {
        super.onResume()
        getSummary()
    }
    private fun getSummary() {
       activity?.let {
           model.getSummary().observe(it){response->
               when(response.status){
                   ApiStatus.SUCCESS -> {
                       binding.shimmerLayout.startShimmer()

                       // CommonFuctions.dismissDialog()
                       chatList.clear()
                       chatList.addAll(response?.data?.data!!.chatSummaryList)
                       adapter.notifyDataSetChanged()
                       binding.shimmerLayout.visibility=View.GONE
                       binding.recyclerview.visibility=View.VISIBLE
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

    override fun chat(position: Int, userId: String,name:String) {

        val intent = Intent(requireActivity(), ChatActivity::class.java)
        intent.putExtra("data", userId)
        intent.putExtra("name",name)
        startActivity(intent)
    }


}