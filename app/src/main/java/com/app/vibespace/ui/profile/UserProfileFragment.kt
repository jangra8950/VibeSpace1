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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.R
import com.app.vibespace.databinding.FragmentUserProfileBinding
import com.app.vibespace.models.profile.UserUpdateModel
import com.app.vibespace.ui.registration.HomeActivity
import com.app.vibespace.ui.registration.SignInActivity
import com.app.vibespace.util.CommonFuctions
import com.app.vibespace.util.MyApp
import com.app.vibespace.util.MyApp.Companion.profileData
import com.app.vibespace.util.showToast
import com.app.vibespace.viewModel.profile.UserProfileViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private lateinit var binding:FragmentUserProfileBinding
    private val model:UserProfileViewModel by viewModels()
    private val args:UserProfileFragmentArgs by navArgs()
  //  private var profileData:UserUpdateModel.Data?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(!::binding.isInitialized)
            binding=DataBindingUtil.inflate(inflater,R.layout.fragment_user_profile,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragment=this

        binding.lifecycleOwner=this

           // profileData = Gson().fromJson(args.dataModel, UserUpdateModel.Data::class.java)

        setValues()

         binding.btnLogOut.setOnClickListener {
             showDialogLogOut(it.context)
          //   logout()
         }
        binding.btnDeleteAccount.setOnClickListener {
            showDialogDelete(it.context)
          //  deleteAccount()
        }

        binding.back.setOnClickListener {
           requireActivity().onBackPressed()
        }

        binding.ivEditProfile.setOnClickListener {
//           val action=UserProfileFragmentDirections.actionUserProfileFragmentToEditProfileFragment(
//               university = profileData?.universityName.toString(),
//               sexuality = profileData?.gender.toString(),
//               age = "29",
//               height = profileData?.height.toString(),
//               occupation = profileData?.occupation.toString()
//           )
//            findNavController().navigate(action)

            (requireActivity() as HomeActivity).updateFragment(EditProfileFragment())
        }
    }

    private fun showDialogLogOut(context: Context){
        CommonFuctions.dialog = Dialog(context)
        CommonFuctions.dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        CommonFuctions.dialog?.setContentView(R.layout.layout_logout_confirm)
        CommonFuctions.dialog?.setCancelable(false)
        CommonFuctions.dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        CommonFuctions.dialog!!.findViewById<Button>(R.id.btnYes).setOnClickListener {
            logout()
            CommonFuctions.dialog!!.dismiss()
        }
        CommonFuctions.dialog!!.findViewById<Button>(R.id.btnNo).setOnClickListener {
            CommonFuctions.dialog!!.dismiss()
        }
        CommonFuctions.dialog?.show()
    }

    private fun showDialogDelete(context: Context){
        CommonFuctions.dialog = Dialog(context)
        CommonFuctions.dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        CommonFuctions.dialog?.setContentView(R.layout.layout_logout_confirm)
        CommonFuctions.dialog?.setCancelable(false)
        CommonFuctions.dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        CommonFuctions.dialog!!.findViewById<Button>(R.id.btnYes).setOnClickListener {
            deleteAccount()
            CommonFuctions.dialog!!.dismiss()
        }
        CommonFuctions.dialog!!.findViewById<Button>(R.id.btnNo).setOnClickListener {
            CommonFuctions.dialog!!.dismiss()
        }
        CommonFuctions.dialog?.show()
    }

    private fun logout() {
        activity?.let {
            model.logOut().observe(it){response->
                when(response.status){
                    ApiStatus.SUCCESS -> {
                        CommonFuctions.dismissDialog()

                        MyApp.sharedpreferences.edit().clear().apply()
                        startActivity(Intent(requireActivity(), SignInActivity::class.java))
                        response.data?.data?.message?.let { it2-> showToast(requireActivity(),it2) }
                        requireActivity().finish()
                    }
                    ApiStatus.ERROR -> {
                        CommonFuctions.dismissDialog()
                        response.message?.let { it1-> showToast(requireActivity(),it1) }
                    }

                    ApiStatus.LOADING ->
                    {
                        CommonFuctions.showDialog(requireActivity())

                    }
                }

            }
        }

    }

    private fun deleteAccount(){
        activity?.let {
            model.deleteAccount().observe(it){response->
                when(response.status){
                    ApiStatus.SUCCESS -> {
                        CommonFuctions.dismissDialog()

                        MyApp.sharedpreferences.edit().clear().apply()
                        startActivity(Intent(requireActivity(), SignInActivity::class.java))
                        response.data?.data?.message?.let { it2-> showToast(requireActivity(),it2) }
                        requireActivity().finish()
                    }
                    ApiStatus.ERROR ->{
                        CommonFuctions.dismissDialog()
                        response.message?.let { it1-> showToast(requireActivity(),it1) }
                    }
                    ApiStatus.LOADING ->{
                        CommonFuctions.showDialog(requireActivity())
                    }
                }

            }
        }
    }

    private fun setValues() {

        // mainFragment(getProfileApi)
     binding.tvProfileName.text= profileData?.firstName+" "+profileData?.lastName
     binding.tvFollowersCount.text=profileData?.totalFollower.toString()
     binding.tvFollowingCount.text=profileData?.totalFollowing.toString()
     binding.tvDataUniversity.text=profileData?.universityName.toString()

       // from editFragment(put Api)
     binding.tvDataSexuality.text=profileData?.gender
     binding.tvDataHeight.text= profileData?.height.toString()+" "+"Ft"
     binding.tvDataOccupation.text=profileData?.occupation

    }


}