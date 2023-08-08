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
import com.app.vibespace.ui.profile.FeedFragment
import com.app.vibespace.util.showToast


class PostAllAdapter(private val mList:ArrayList<PostListModel.Data.Post>,
                    private val post: Post
): RecyclerView.Adapter<PostAllAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val caption: TextView = itemView.findViewById(R.id.tvCaption)
        val name: TextView = itemView.findViewById(R.id.tvName)
        val likeCount: TextView = itemView.findViewById(R.id.tvLikeCount)
        val commentCount: TextView = itemView.findViewById(R.id.tvCommentCount)
        val menu: ImageView = itemView.findViewById(R.id.ivMore)
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
        holder.menu.setOnClickListener {

            showMenu(holder.menu,it.context,position,item.userId)

        }
    }

    private fun showMenu(deletePost: ImageView, context: Context, position: Int, userId: String) {
        val popupMenu = PopupMenu(context, deletePost)

        popupMenu.menuInflater.inflate(R.menu.menu_items, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
              R.id.blockUser->{
                post.block(position,userId)

              }
              R.id.reportPost->{
                  showToast(context,"Report Clicked")

              }
            }
            true
        }

        try {
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menuPopupHelper = popup.get(popupMenu)
            val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
            val setForceIcons = classPopupHelper.getMethod("setForceShowIcon", Boolean::class.java)
            setForceIcons.invoke(menuPopupHelper, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // Showing the popup menu
        popupMenu.show()
    }

    interface Post{
        fun block(position: Int,userId: String)
    }

}