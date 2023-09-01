package com.app.vibespace.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.vibespace.databinding.LayoutChatItemBinding

import com.app.vibespace.models.profile.SummaryModel
import com.squareup.picasso.Picasso

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import java.text.SimpleDateFormat

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
//        if (position>=0)
//            holder.binding.data=mList[position]
        val item=mList[position]

        if(item.isOwnMessage)
            holder.binding.tvName.text=item.receiverFirstName+" "+item.receiverLastName
        else
           holder.binding.tvName.text=item.senderFirstName+" "+item.senderLastName
        holder.binding.tvTxt.text=item.message
        holder.itemView.setOnClickListener {
            if(item.isOwnMessage)
              chat.chat(position,item.receiverId,item.receiverFirstName+" "+item.receiverLastName)
            else
                chat.chat(position,item.senderId,item.senderFirstName+" "+item.senderLastName)
        }

        holder.binding.tvTime.text=convertTimestampToRealTime(item.sentAt.toLong())

     //   Picasso.with(context).load(item.senderMascotIcon).into(holder.binding.ivAvatar)

//        CoroutineScope(Dispatchers.Main).launch {
//            try {
//                val image = loadImageFromUrl(item.senderMascotIcon)
//                if (image != null)
//                    holder.binding.ivAvatar.setImageBitmap(image)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }

    }

    fun convertTimestampToRealTime(timestamp: Long): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("HH:mm", Locale.US)
        format.timeZone = TimeZone.getDefault()
        return format.format(date)
    }

    private suspend fun loadImageFromUrl(imageUrl: String): Bitmap? = withContext(Dispatchers.IO) {
        return@withContext try {
            val `in` = URL(imageUrl).openStream()
            BitmapFactory.decodeStream(`in`)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    interface Summary{
        fun chat(position: Int,userId: String,name:String)
    }



}