package com.app.vibespace.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.R
import com.app.vibespace.adapter.ChatListAdapter

import com.app.vibespace.databinding.FragmentChatBinding
import com.app.vibespace.models.profile.SummaryModel
import com.app.vibespace.util.showToast
import com.app.vibespace.viewModel.profile.ChatListViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment(), ChatListAdapter.Summary {

    private lateinit var binding:FragmentChatBinding
    private lateinit var adapter: ChatListAdapter
    private val model:ChatListViewModel by viewModels()
    private var chatList=ArrayList<SummaryModel.Data.ChatSummary>()
    private var allchatList=ArrayList<SummaryModel.Data.ChatSummary>()


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

        searchFunctionality()

        dragFunctionality()

    }

    private fun dragFunctionality() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val deletedCourse: SummaryModel.Data.ChatSummary =
                    chatList[viewHolder.bindingAdapterPosition]

                deleteChat(deletedCourse.conversationId,viewHolder.bindingAdapterPosition)

            }
        }).attachToRecyclerView(binding.recyclerview)

    }

    private fun searchFunctionality() {
        binding.etSearch.addTextChangedListener(
            afterTextChanged = {

            },
            onTextChanged = { s, start, before, count ->
                chatList.clear()
                if (!s.isNullOrEmpty())
                    chatList.addAll(allchatList.filter {
                        if (it.isOwnMessage)
                        it.receiverFirstName!=null && (it.receiverFirstName.contains(
                            s.toString(),
                            true) || it.receiverLastName.contains(
                            s.toString(),
                            true))
                        else it.senderFirstName!=null && (it.senderFirstName.contains(
                            s.toString(),
                            true) || it.senderLastName.contains(
                            s.toString(),
                            true))
                    })
                else
                    chatList.addAll(allchatList)
                adapter.notifyDataSetChanged()
            },
            beforeTextChanged = { s, start, before, count ->

            }
        )
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

                       chatList.clear()
                       allchatList.clear()
                       chatList.addAll(response?.data?.data!!.chatSummaryList)
                       allchatList.addAll(response?.data?.data!!.chatSummaryList)
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

    private fun deleteChat(id: String, bindingAdapterPosition: Int){
        activity?.let {
            model.deleteChat(id).observe(it){response->
                when(response.status){
                    ApiStatus.SUCCESS -> {
                        chatList.removeAt(bindingAdapterPosition)
                        adapter.notifyItemRemoved(bindingAdapterPosition)
                    }
                    ApiStatus.ERROR -> {

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