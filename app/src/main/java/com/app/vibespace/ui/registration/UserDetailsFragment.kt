package com.app.vibespace.ui.registration


import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.Enums.CreateOtpType
import com.app.vibespace.R
import com.app.vibespace.databinding.FragmentUserDetailsBinding
import com.app.vibespace.models.registration.UniversityListModel
import com.app.vibespace.service.Resources
import com.app.vibespace.util.ColorArrayAdapter
import com.app.vibespace.util.CommonFuctions
import com.app.vibespace.util.MyApp

import com.app.vibespace.util.showToast
import com.app.vibespace.viewModel.registration.UserDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserDetailsFragment : Fragment() {
    private val array=listOf("Harvard","Boston","Emerson","Oxford")

    private var universityList=ArrayList<UniversityListModel.Data.University>()
    private var universityId=""

    private lateinit var binding:FragmentUserDetailsBinding

    private val model:UserDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!::binding.isInitialized) {
            binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_user_details, container, false)

        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.viewModel = model
        binding.fragment = this
        binding.lifecycleOwner = this

        getUniversityList(view)

        view.setOnClickListener { v ->
            binding.etDob.clearFocus()
            binding.dropDown.clearFocus()
            binding.etBio.clearFocus()
            val imm =
                requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }



private fun dropDown(autoCompleteTextView: AutoCompleteTextView) {
    val data = ArrayList<String>()
    universityList.forEach{
        data.add(it.universityName)
    }
    val adapter = ColorArrayAdapter(autoCompleteTextView.context, android.R.layout.simple_list_item_1, data)

    autoCompleteTextView.setAdapter(adapter)
    autoCompleteTextView.onItemClickListener =
        AdapterView.OnItemClickListener { _, _, position, _ ->
            adapter.setSelectedItem(position)
            universityId=universityList[position]._id

        }
}


    fun onContinue(view:View){
        if(model.checkValue(view)){
             setdata()
            val action=UserDetailsFragmentDirections.actionUserDetailsFragmentToVerifyEmailFragment(
                otpType = CreateOtpType.UR.name
            )
            findNavController().navigate(action)
        }

    }
    private fun setdata(){
        (requireActivity() as SignInActivity).data.dob=model.dob.value.toString()
        (requireActivity() as SignInActivity).data.bio=model.bio.value.toString()
        (requireActivity() as SignInActivity).data.universityId=universityId
    }

    fun onSignIn(view:View){
        findNavController().navigate(R.id.signInFragment)
    }

    private fun getUniversityList(view:View){

        activity?.let {
            model.getUniversityList().observe(it){response->
                when(response.status){

                    ApiStatus.ERROR -> {
                        CommonFuctions.dismissDialog()
                       // binding.progressBar.root.visibility=View.GONE
                        response.message?.let { it1 -> showToast(requireActivity(), it1) }
                    }

                    ApiStatus.SUCCESS -> {
                        CommonFuctions.dismissDialog()
                        //binding.progressBar.root.visibility=View.GONE
                        universityList.addAll(response.data?.data?.universities!!)
                        dropDown(binding.dropDown)

                    }
                    ApiStatus.LOADING->{
                        CommonFuctions.showDialog(requireActivity())
                      //  binding.progressBar.root.visibility=View.VISIBLE
                    }
                }

            }
        }
    }
    fun onBack(view: View){
        requireActivity().onBackPressed()
    }


}