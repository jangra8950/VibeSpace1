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
import com.app.vibespace.ui.profile.ProfileMainFragment
import com.app.vibespace.util.CommonFuctions.Companion.loadImage
import com.app.vibespace.util.MyApp
import com.app.vibespace.util.CommonFuctions.Companion.showDialogLogOutt
import com.app.vibespace.util.CommonFuctions.Companion.showMenuItems

class PostAdapter(private val mList:ArrayList<PostListModel.Data.Post>,
                  private val callBack:PostCallbacks, val context: Context
                  ): RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val caption: TextView = itemView.findViewById(R.id.tvCaption)
        val name: TextView = itemView.findViewById(R.id.tvName)
        val likeCount: TextView = itemView.findViewById(R.id.tvLikeCount)
        val commentCount: TextView = itemView.findViewById(R.id.tvCommentCount)
        val deletePost: ImageView = itemView.findViewById(R.id.ivMore)
        val pic: ImageView = itemView.findViewById(R.id.ivMap)
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
        holder.likeCount.text= item.likeCount
        holder.commentCount.text=item.commentCount
        holder.deletePost.setOnClickListener {

            showMenuItems(holder.deletePost,context,"Are you sure you want to Delete this Post?",R.menu.menu_delete_post){
                callBack.deletePostCallback(position,item.postId)
            }
        }

        loadImage(context, MyApp.profileData!!.profilePic,holder.pic)
    }

    interface PostCallbacks{
        fun deletePostCallback(position: Int,postId: String)
    }

}