package com.app.vibespace.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.vibespace.databinding.LayoutUserListBinding
import com.app.vibespace.models.setting.FollowersModel
import com.app.vibespace.util.CommonFuctions.Companion.loadImage

class NewChatUserListAdapter(private val mList:ArrayList<FollowersModel.Data.Follower>,
                             private val context: Context,private val newChat:NewChat
):RecyclerView.Adapter<NewChatUserListAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: LayoutUserListBinding): RecyclerView.ViewHolder(binding.root){

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewChatUserListAdapter.ViewHolder {
        val view = LayoutUserListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewChatUserListAdapter.ViewHolder, position: Int) {
        val item=mList[position]
        holder.binding.tvName.text= item.userDetails.firstName
        loadImage(context, item.userDetails.profilePic, holder.binding.ivAvatar)


        holder.itemView.setOnClickListener {
            newChat.newChat(item.userDetails.userId,item.userDetails.profilePic,item.userDetails.firstName)
        }
    }

    override fun getItemCount(): Int {
       return mList.size
    }

    interface NewChat{
        fun newChat(userId:String,image:String,name:String)
    }
}