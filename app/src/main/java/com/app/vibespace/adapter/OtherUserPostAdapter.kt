package com.app.vibespace.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.vibespace.R
import com.app.vibespace.models.profile.PostListModel

class OtherUserPostAdapter(private val mList:ArrayList<PostListModel.Data.Post>
): RecyclerView.Adapter<OtherUserPostAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val caption: TextView = itemView.findViewById(R.id.tvCaption)
        val name: TextView = itemView.findViewById(R.id.tvName)
        val likeCount: TextView = itemView.findViewById(R.id.tvLikeCount)
        val commentCount: TextView = itemView.findViewById(R.id.tvCommentCount)
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

    }


}