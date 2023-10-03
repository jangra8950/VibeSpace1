package com.app.vibespace.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView


import androidx.recyclerview.widget.RecyclerView
import com.app.vibespace.R
import com.app.vibespace.models.profile.UserListModel
import com.app.vibespace.util.CommonFuctions.Companion.loadImage

class UserListAdapter(private val mList:ArrayList<UserListModel.Data.User>,
                      private val shift:UserProfile, private val context: Context

): RecyclerView.Adapter<UserListAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val name: TextView = itemView.findViewById(R.id.tvName)
        val pic: AppCompatImageView = itemView.findViewById(R.id.ivAvatar)
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
        loadImage(context,item.profilePic,holder.pic)
        holder.itemView.setOnClickListener {
           shift.user(item.userId)
        }

    }

    interface UserProfile{
        fun user(id:String)
    }

}