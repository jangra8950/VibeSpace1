package com.app.vibespace.ui.chat

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.R
import com.app.vibespace.adapter.NewChatUserListAdapter
import com.app.vibespace.adapter.UserListAdapter
import com.app.vibespace.databinding.ActivityNewChatBinding
import com.app.vibespace.models.profile.UserListModel
import com.app.vibespace.models.setting.FollowersModel
import com.app.vibespace.ui.profile.ChatActivity
import com.app.vibespace.util.showToast
import com.app.vibespace.viewModel.profile.NewChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewChatActivity : AppCompatActivity(),NewChatUserListAdapter.NewChat {

    private lateinit var binding:ActivityNewChatBinding
    private val model:NewChatViewModel by viewModels()
    private var followerList=ArrayList<FollowersModel.Data.Follower>()
    private var allfollowerList=ArrayList<FollowersModel.Data.Follower>()
    private lateinit var adapter:NewChatUserListAdapter
    private var mess:String?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityNewChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mess = intent.getStringExtra("mess")

        binding.recyclerview.layoutManager= LinearLayoutManager(this)
        adapter= NewChatUserListAdapter(followerList,this,this)
        binding.recyclerview.adapter=adapter

        binding.root.setOnClickListener { v ->
            binding.etSearch.clearFocus()

            val imm =
                this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }

        binding.back.setOnClickListener {
            onBackPressed()
        }

        searchFunctionality()
        getFollower()

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
                adapter.notifyDataSetChanged()
            },
            beforeTextChanged = { s, start, before, count ->

            }
        )
    }

    private fun getFollower() {
            model.getFollowers().observe(this){response->
                when(response.status){
                    ApiStatus.SUCCESS -> {
                        binding.shimmerLayout.startShimmer()
                        binding.shimmerLayout.visibility=View.GONE
                        binding.recyclerview.visibility=View.VISIBLE
                        followerList.clear()
                        allfollowerList.clear()
                        followerList.addAll(response?.data?.data!!.followers)
                        allfollowerList.addAll(response.data.data.followers)

                    }
                    ApiStatus.ERROR ->{
                        response.message?.let { it1 -> showToast(this, it1) }
                    }
                    ApiStatus.LOADING -> {

                    }
                }
            }

    }

    override fun newChat(userId: String, image: String, name: String) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("data", userId)
        intent.putExtra("name", name)
        intent.putExtra("image", image)
        intent.putExtra("mess", mess)
        startActivity(intent)
    }
}