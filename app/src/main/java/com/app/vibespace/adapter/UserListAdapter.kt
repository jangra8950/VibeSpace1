package com.app.vibespace.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.vibespace.R
import com.app.vibespace.models.profile.UserListModel
import com.app.vibespace.models.setting.BlockedUserListModel
import com.app.vibespace.ui.profile.ChatActivity

class UserListAdapter(private val mList:ArrayList<UserListModel.Data.User>,var activity:Activity

): RecyclerView.Adapter<UserListAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val name: TextView = itemView.findViewById(R.id.tvName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.layout_user_list,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=mList[position]
        holder.name.text= item.firstName

        holder.itemView.setOnClickListener {
            activity.startActivity(Intent(activity,ChatActivity::class.java).putExtra("data",mList[position].userId))
        }

    }

}