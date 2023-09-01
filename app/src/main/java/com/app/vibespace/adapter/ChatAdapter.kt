package com.app.vibespace.adapter

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.vibespace.databinding.LayoutChatReceiverBinding
import com.app.vibespace.databinding.LayoutHeaderDateBinding
import com.app.vibespace.models.profile.ChatItemModel
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ChatAdapter(private val mList:ArrayList<ChatItemModel.Data.Chat>,
                  var context: Context, var otherUserId:String) :
    RecyclerView.Adapter<ChatAdapter.ReceiverViewHolder>(), StickyRecyclerHeadersAdapter<ChatAdapter.HeaderViewHolder> {

    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // Get the date of the chat item in milliseconds
    private fun getChatDate(timestamp: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.timeInMillis
    }

    inner class ReceiverViewHolder(var binding: LayoutChatReceiverBinding): RecyclerView.ViewHolder(binding.root){

    }

    class HeaderViewHolder(var binding:LayoutHeaderDateBinding) : RecyclerView.ViewHolder(binding.root) {

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiverViewHolder {
        val view=LayoutChatReceiverBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ReceiverViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatAdapter.ReceiverViewHolder, position: Int) {
        if (mList[position].receiverId==otherUserId) {
            holder.binding.sender.visibility=View.VISIBLE
            holder.binding.receiver.visibility=View.GONE
            holder.binding.tvSenderName.text=mList[position].message
            holder.binding.tvSenderTime.text=convertTimestampToRealTime(mList[position].sentAt)

        } else {
            holder.binding.sender.visibility=View.GONE
            holder.binding.receiver.visibility=View.VISIBLE
            holder.binding.tvReceiverName.text=mList[position].message
            holder.binding.tvReceiverTime.text=convertTimestampToRealTime(mList[position].sentAt)
        }

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    // Sticky header related methods

    override fun getHeaderId(position: Int): Long {
        val item = mList[position]
        return getChatDate(item.sentAt)
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup): HeaderViewHolder {
        val view =LayoutHeaderDateBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HeaderViewHolder(view)
    }

    override fun onBindHeaderViewHolder(holder: HeaderViewHolder, headerPosition: Int) {
       // holder.binding.tvHeaderDate.text=convertTimestamp(mList[headerPosition].sentAt)
        val headerDateInMillis = mList[headerPosition].sentAt
        val headerDateString = dateFormatter.format(Date(headerDateInMillis))
        holder.binding.tvHeaderDate.text = headerDateString
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
}
