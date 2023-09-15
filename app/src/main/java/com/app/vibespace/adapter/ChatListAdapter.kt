package com.app.vibespace.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.vibespace.R
import com.app.vibespace.databinding.LayoutChatItemBinding

import com.app.vibespace.models.profile.SummaryModel
import com.app.vibespace.util.CommonFuctions.Companion.convertTimestampToRealTime
import com.app.vibespace.util.CommonFuctions.Companion.loadImage
import com.squareup.picasso.Picasso

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar

import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ChatListAdapter(private val mList:ArrayList<SummaryModel.Data.ChatSummary>,
                      var context: Context,private val chat:Summary
): RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: LayoutChatItemBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=  LayoutChatItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item=mList[position]

        if(item.isOwnMessage)
        {
            holder.binding.tvName.text=item.receiverFirstName+" "+item.receiverLastName
            if (!item.receiverMascotIcon.isNullOrEmpty())
                loadImage(context,item.receiverMascotIcon,holder.binding.ivAvatar)

        }

        else{
            holder.binding.tvName.text=item.senderFirstName+" "+item.senderLastName
            if (!item.senderMascotIcon.isNullOrEmpty())
                loadImage(context,item.senderMascotIcon,holder.binding.ivAvatar)
        }


        holder.binding.tvTxt.text=item.message
        holder.binding.tvTime.text=convertTimestampToRealTime(item.sentAt)

        holder.itemView.setOnClickListener {
            if(item.isOwnMessage)
              chat.chat(position,item.receiverId,item.receiverFirstName+" "+item.receiverLastName,item.receiverMascotIcon)
            else
                chat.chat(position,item.senderId,item.senderFirstName+" "+item.senderLastName,item.senderMascotIcon)
        }






    }



    interface Summary{
        fun chat(position: Int,userId: String,name:String,image:String)
    }



}