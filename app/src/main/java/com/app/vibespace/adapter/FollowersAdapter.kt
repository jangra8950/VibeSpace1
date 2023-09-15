package com.app.vibespace.adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.app.vibespace.R
import com.app.vibespace.databinding.LayoutFollowersListBinding
import com.app.vibespace.models.setting.FollowersModel
import com.app.vibespace.util.CommonFuctions
import com.app.vibespace.util.CommonFuctions.Companion.loadImage
import com.app.vibespace.util.CommonFuctions.Companion.showDialogLogOutt
import com.app.vibespace.util.CommonFuctions.Companion.showMenuItems
import java.util.ArrayList

class FollowersAdapter(private val context: Context, private val change:Follow
): RecyclerView.Adapter<FollowersAdapter.ViewHolder>()  {
    var mList=ArrayList<FollowersModel.Data.Follower>()
    var followType="follower"
    inner class ViewHolder(var binding: LayoutFollowersListBinding): RecyclerView.ViewHolder(binding.root){

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersAdapter.ViewHolder {
        val view=  LayoutFollowersListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowersAdapter.ViewHolder, position: Int) {
        if (followType=="follower") {
            holder.binding.btnRemove.visibility = View.VISIBLE
            holder.binding.cgGroup.visibility = View.GONE
        }else{
            holder.binding.btnRemove.visibility = View.GONE
            holder.binding.cgGroup.visibility = View.VISIBLE
        }
        val item=mList[position]
        holder.binding.tvName.text=item.userDetails.firstName
        loadImage(context,item.userDetails.profilePic,holder.binding.ivAvatar)

        holder.binding.btnRemove.setOnClickListener {
            showDialogLogOutt(context,"Are you sure to want to unfollow this User?"){
                change.unfollow(item.userDetails.userId,position)
            }
        }

       holder.binding.ivMenu.setOnClickListener {
           showMenuItems(holder.binding.ivMenu,context,"Are you sure to want to Block this User?",R.menu.menu_items){
               change.block(item.userDetails.userId,position)
           }
       }
    }

    fun updateData(list:ArrayList<FollowersModel.Data.Follower>,follow:String){
        mList.clear()
        mList.addAll(list)
        followType=follow
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
       return mList.size
    }

    interface Follow{
        fun unfollow(id:String,position: Int)
        fun block(userId: String,position: Int)
    }

}