package com.app.vibespace.ui.profile

import android.R.attr.value
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.vibespace.R
import com.app.vibespace.databinding.FragmentPriceProfileBinding
import com.app.vibespace.util.CommonFuctions
import com.app.vibespace.util.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PriceProfileFragment : Fragment() {

    private lateinit var binding:FragmentPriceProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(!::binding.isInitialized)
            binding=DataBindingUtil.inflate(inflater,R.layout.fragment_price_profile,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setValues()

    }

    private fun setValues() {
        binding.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                showToast(requireActivity(),"$progress")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnDone.setOnClickListener {
           CommonFuctions.showDialog1(it.context)
        }

    }


}