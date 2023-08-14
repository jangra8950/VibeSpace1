package com.app.vibespace.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.vibespace.R

import com.app.vibespace.models.setting.BlockedUserListModel


class BlockedUserAdapter(private val mList:ArrayList<BlockedUserListModel.Data.BlockUser>,
                         private val post: Unblock
): RecyclerView.Adapter<BlockedUserAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val name: TextView = itemView.findViewById(R.id.tvName)
        val menu: Button = itemView.findViewById(R.id.btnBlock)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.layout_blocked_user,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=mList[position]
        holder.name.text= item.blockedUserDetails.firstName


        holder.menu.setOnClickListener {
            post.unblock(position,item.otherUserId)
        }
    }

    interface Unblock{
        fun unblock(position: Int,userId: String)
    }

}