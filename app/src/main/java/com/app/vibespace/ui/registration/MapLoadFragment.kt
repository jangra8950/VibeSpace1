package com.app.vibespace.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.app.vibespace.R
import com.app.vibespace.databinding.FragmentHomeBinding


class MapLoadFragment:Fragment(){
    private lateinit var binding:FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        if(!::binding.isInitialized)
            binding=FragmentHomeBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFragment("all")
        changeMapLoad()
    }


    private fun changeMapLoad() {
        binding.all.setOnClickListener {
            binding.following.setTextColor(ContextCompat.getColor(it.context, R.color.colorEditTxt))
            binding.all.setTextColor(ContextCompat.getColor(it.context, R.color.colorPrimary))
            loadFragment("all")
        }
        binding.following.setOnClickListener {
            binding.all.setTextColor(ContextCompat.getColor(it.context, R.color.colorEditTxt))
            binding.following.setTextColor(ContextCompat.getColor(it.context, R.color.colorPrimary))
            loadFragment("following")
        }
    }


    private fun loadFragment(type:String){
        val bundle=Bundle()
        bundle.putString("type",type)
        val fragment=HomeFragment()
        fragment.arguments=bundle
        childFragmentManager.beginTransaction().replace(R.id.mapView, fragment).addToBackStack(null)
            .commit()
    }

}