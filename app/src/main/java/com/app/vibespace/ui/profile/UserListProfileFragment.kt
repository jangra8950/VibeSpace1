package com.app.vibespace.ui.profile

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.R
import com.app.vibespace.adapter.UserListAdapter
import com.app.vibespace.databinding.ActivityHomeBinding
import com.app.vibespace.databinding.FragmentUserListProfileBinding
import com.app.vibespace.models.profile.UserListModel
import com.app.vibespace.util.showToast
import com.app.vibespace.viewModel.profile.UserListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListProfileFragment : Fragment() {

    private lateinit var binding:FragmentUserListProfileBinding
    private val model: UserListViewModel by viewModels()
    private var userList=ArrayList<UserListModel.Data.User>()
    private var allUserList=ArrayList<UserListModel.Data.User>()
    private lateinit var adapter:UserListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(!::binding.isInitialized)
            binding=DataBindingUtil.inflate(inflater,R.layout.fragment_user_list_profile,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner=this

        binding.recyclerview.layoutManager=LinearLayoutManager(activity)
        adapter= UserListAdapter(userList)
        binding.recyclerview.adapter=adapter

        view.setOnClickListener { v ->
            binding.etSearch.clearFocus()

            val imm =
                requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        searchFunctionality()
        getUserList()

    }

    private fun searchFunctionality() {
        binding.etSearch.addTextChangedListener(
            afterTextChanged = {

            },
            onTextChanged = { s, start, before, count ->
                userList.clear()
                if (!s.isNullOrEmpty())
                    userList.addAll(allUserList.filter {
                        it.firstName.contains(
                            s.toString(),
                            true
                        )
                    })
                else
                    userList.addAll(allUserList)
                adapter.notifyDataSetChanged()
            },
            beforeTextChanged = { s, start, before, count ->

            }
        )
    }

    private fun getUserList() {
      activity?.let{
          model.getUserList().observe(it){response->
              when(response.status){
                  ApiStatus.SUCCESS -> {

                      binding.shimmerLayout.startShimmer()
                      binding.shimmerLayout.visibility=View.GONE
                      binding.recyclerview.visibility=View.VISIBLE
                      userList.clear()
                      allUserList.clear()
                      userList.addAll(response?.data?.data!!.users)
                      allUserList.addAll(response?.data?.data!!.users)
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

}