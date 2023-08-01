package com.app.vibespace.ui.profile

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.R
import com.app.vibespace.databinding.FragmentEditProfileBinding
import com.app.vibespace.models.registration.UniversityListModel
import com.app.vibespace.util.ApiConstants
import com.app.vibespace.util.ColorArrayAdapter
import com.app.vibespace.util.CommonFuctions
import com.app.vibespace.util.MyApp
import com.app.vibespace.util.MyApp.Companion.profileData
import com.app.vibespace.util.showToast
import com.app.vibespace.viewModel.profile.EditProfileViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private val array=listOf("Male","Female","Other")

    private var universityList=ArrayList<UniversityListModel.Data.University>()
    var universityId=""

    private lateinit var binding:FragmentEditProfileBinding
    private val model:EditProfileViewModel by viewModels()
    private val args:EditProfileFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       if(!::binding.isInitialized)
           binding=DataBindingUtil.inflate(inflater,R.layout.fragment_edit_profile,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragment=this
        binding.lifecycleOwner=this
        binding.viewModel=model

        view.setOnClickListener { v ->
            binding.dropDown.clearFocus()
            binding.dropDown1.clearFocus()
            binding.etAge.clearFocus()
            binding.etHeight.clearFocus()
            binding.etOccuption.clearFocus()
            val imm =
                requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)

        }

       // setValues()
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        getUniversityList(view)
        model.setArguments(arguments,universityId)
        binding.dropDown1.setOnClickListener {
            dropDown1(binding.dropDown1)
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

    private fun dropDown1(autoCompleteTextView: AutoCompleteTextView) {

        val adapter = ColorArrayAdapter(autoCompleteTextView.context, android.R.layout.simple_list_item_1, array)
        autoCompleteTextView.setAdapter(adapter)

        autoCompleteTextView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                adapter.setSelectedItem(position)
            }
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

     fun save(view:View){
      activity?.let{
          model.updateUser().observe(it){response->
              when(response.status){
                  ApiStatus.SUCCESS -> {
                      CommonFuctions.dismissDialog()
//                     val action=EditProfileFragmentDirections.actionEditProfileFragmentToUserProfileFragment(
//                         dataModel = Gson().toJson(response.data?.data),
//                         sexuality = response.data?.data?.gender.toString(),
//                         height = response.data?.data?.height.toString(),
//                         occupation = response.data?.data?.occupation.toString()
//                     )
                      val editor = MyApp.sharedpreferences.edit()
                      profileData?.gender=response.data?.data?.gender.toString()
                      profileData?.height=response.data?.data?.height!!.toDouble()
                      profileData?.occupation=response.data?.data?.occupation.toString()

                      editor.putString(ApiConstants.PROFILE_DATA, Gson().toJson(profileData))
                      editor.apply()
                      findNavController().navigate(R.id.userProfileFragment)

                  }
                  ApiStatus.ERROR -> {
                      CommonFuctions.dismissDialog()
                      // binding.progressBar.root.visibility=View.GONE
                      response.message?.let { it1 -> showToast(requireActivity(), it1) }
                  }
                  ApiStatus.LOADING -> {
                      CommonFuctions.showDialog(requireActivity())
                  }
              }

          }
      }
    }

    private fun setValues() {
        binding.etAge.setText(args.age)
        binding.etHeight.setText(args.height)
        binding.etOccuption.setText(args.occupation)
        binding.dropDown.setText(args.university)
        binding.dropDown1.setText(args.sexuality)
    }

}