package com.app.vibespace.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.vibespace.R
import com.app.vibespace.models.profile.PostListModel

import com.app.vibespace.util.CommonFuctions.Companion.loadImage
import com.app.vibespace.util.CommonFuctions.Companion.showDialogLogOutt
import com.app.vibespace.util.CommonFuctions.Companion.showMenuItems

class OtherUserPostAdapter(private val mList:ArrayList<PostListModel.Data.Post>,
                           var context: Context, private val change: ChangesCallBack
): RecyclerView.Adapter<OtherUserPostAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val caption: TextView = itemView.findViewById(R.id.tvCaption)
        val name: TextView = itemView.findViewById(R.id.tvName)
        val likeCount: TextView = itemView.findViewById(R.id.tvLikeCount)
        val commentCount: TextView = itemView.findViewById(R.id.tvCommentCount)
        val pic: AppCompatImageView = itemView.findViewById(R.id.ivMap)
       // val mascot: AppCompatImageView = itemView.findViewById(R.id.ivAvatar)
        val menu: ImageView = itemView.findViewById(R.id.ivMore)
      // val typeText: AppCompatEditText =itemView.findViewById(R.id.tvTypeComment)
        val chatAct: AppCompatImageView = itemView.findViewById(R.id.ivChatAct)
        val chatNew: AppCompatImageView = itemView.findViewById(R.id.ivChatNew)
        val chatNewAct: AppCompatImageView = itemView.findViewById(R.id.ivChatNewAct)
        val like:AppCompatImageView = itemView.findViewById(R.id.ivLike)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.layout_post_recycler_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=mList[position]
        holder.name.text= "${item.userDetails.firstName} ${item.userDetails.lastName}"
        holder.caption.text= item.caption
        holder.likeCount.text= item.likeCount.toString()
        holder.commentCount.text=item.commentCount.toString()
       // holder.typeText.visibility=View.GONE
        Log.i("CXCXCCX",item.userDetails.profilePic+",   ,"+"$position")

       // loadImage(context,item.userDetails.mascotIcon,holder.mascot)
        loadImage(context,item.userDetails.profilePic,holder.pic)

        holder.menu.setOnClickListener {

            showMenuItems(holder.menu,context,"Are you sure to want to Block this user?",R.menu.menu_items){
                change.block(item.userId,position)
            }
        }

        holder.chatAct.setOnClickListener {
            change.chat(item.userId,item.userDetails.profilePic,item.userDetails.firstName+" "+item.userDetails.lastName, item.caption)
        }

        holder.chatNewAct.setOnClickListener {
            change.newChat(item.userId,item.userDetails.profilePic,item.userDetails.firstName, item.caption)
        }

        holder.chatNew.setOnClickListener {
            showDialogLogOutt(context,"Are you sure to want to add this vibe to your Profile?",click={
                change.vibe(item.caption,item.postVisibility)
            })
        }

        holder.like.setOnClickListener {
            if (item.isLiked)
                change.unlike(item.postId,position)
            else
                change.like(item.postId,position)
        }

        if (item.isLiked)
            ImageViewCompat.setImageTintList(holder.like, ColorStateList.valueOf(
                context.getColor(
                    R.color.colorRed)))
        else
            ImageViewCompat.setImageTintList(holder.like, ColorStateList.valueOf(context.getColor(
                R.color.colorTxt)))
    }


    interface ChangesCallBack{
        fun block(userId: String,position: Int)
        fun chat(userId:String,image:String,name:String, mess:String)
        fun vibe(caption:String, postVisibility:String)
        fun like(postId:String,position: Int)
        fun unlike(postId:String,position: Int)
        fun newChat(userId:String,image:String,name:String, mess:String)
    }

}