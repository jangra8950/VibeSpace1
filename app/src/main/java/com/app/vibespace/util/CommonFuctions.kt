package com.app.vibespace.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import com.app.vibespace.R
import com.app.vibespace.ui.profile.UserProfileFragment

class CommonFuctions {
    companion object {
        var dialog: Dialog?=null
        fun showDialog(context: Context){
            if (dialog!=null)
                dialog?.dismiss()
            dialog = Dialog(context)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(R.layout.layout_progress_bar)
            dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.show()
        }

        fun dismissDialog(){
            if (dialog!=null)
                dialog?.dismiss()
        }

        fun showDialog1(context: Context){
            dialog = Dialog(context)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(R.layout.layout_price_confirmation)
            dialog?.setCancelable(false)
            dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog!!.findViewById<Button>(R.id.btnYes).setOnClickListener {
                showToast(context,"Yes Button Clicked")
                dialog!!.dismiss()
            }
            dialog!!.findViewById<Button>(R.id.btnNo).setOnClickListener {
                dialog!!.dismiss()
            }
            dialog?.show()
        }



        fun showDialogDelete(context: Context){
            dialog = Dialog(context)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(R.layout.layout_logout_confirm)
            dialog?.setCancelable(false)
            dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog!!.findViewById<Button>(R.id.btnYes).setOnClickListener {

                dialog!!.dismiss()
            }
            dialog!!.findViewById<Button>(R.id.btnNo).setOnClickListener {
                dialog!!.dismiss()
            }
            dialog?.show()
        }
    }
}