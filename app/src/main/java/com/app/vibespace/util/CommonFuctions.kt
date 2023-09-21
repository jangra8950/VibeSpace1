package com.app.vibespace.util

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.format.DateFormat
import android.util.DisplayMetrics
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import com.app.vibespace.R
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import java.text.ParseException
import java.util.Calendar
import java.util.TimeZone


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



        fun showDialogConfirmation(context: Context,value:String,click:()->Unit){
            dialog = Dialog(context)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(R.layout.layout_logout_confirm)
            dialog?.setCancelable(false)
            dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.findViewById<AppCompatTextView>(R.id.tvConfirmation).text=value
            dialog!!.findViewById<Button>(R.id.btnYes).setOnClickListener {
                click()
                dialog!!.dismiss()
            }
            dialog!!.findViewById<Button>(R.id.btnNo).setOnClickListener {
                dialog!!.dismiss()
            }
            dialog?.show()
        }

        fun convertTimestampToRealTime(timestamp: Long): String {

            val smsTime: Calendar = Calendar.getInstance(TimeZone.getDefault())
            smsTime.timeInMillis = timestamp
            return try {
                DateFormat.format("hh:mm a", smsTime).toString()
            } catch (e: ParseException) {
                throw RuntimeException(e)
            }
        }

        fun loadImage(context:Context,url:String,imageView: ImageView){
            if(url!="")
                Picasso.with(context).load(url).transform(PicassoCircleTransformation()).into(imageView)
            else
                Picasso.with(context).load(R.drawable.ic_profile_default).transform(PicassoCircleTransformation()).into(imageView)
        }

        suspend fun loadImageFromUrl(imageUrl: String): Bitmap? = withContext(Dispatchers.IO) {
            return@withContext try {
                val `in` = URL(imageUrl).openStream()
                BitmapFactory.decodeStream(`in`)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }


        fun convertPixelsToDp( context: Context,px: Int): Float {
            return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }
    }
}