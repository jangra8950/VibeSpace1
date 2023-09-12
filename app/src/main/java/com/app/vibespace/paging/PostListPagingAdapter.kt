package com.app.vibespace.paging

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.core.widget.ImageViewCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.vibespace.R

import com.app.vibespace.databinding.LayoutPostRecyclerItemBinding
import com.app.vibespace.models.profile.PostListModel
import com.app.vibespace.util.CommonFuctions
import com.app.vibespace.util.CommonFuctions.Companion.loadImage


class PostListPagingAdapter(var context:Context,private val post: Post):PagingDataAdapter<PostListModel.Data.Post,PostListPagingAdapter.PostViewHolder>(PostListDiffCallback) {
    override fun onBindViewHolder(holder: PostListPagingAdapter.PostViewHolder, position: Int) {
        val item: PostListModel.Data.Post? = getItem(position)

        if (item != null) {
            if (item.isLiked)
                ImageViewCompat.setImageTintList(holder.binding.ivLike, ColorStateList.valueOf(
                    context.getColor(
                    R.color.colorRed)))
            else
                ImageViewCompat.setImageTintList(holder.binding.ivLike, ColorStateList.valueOf(context.getColor(
                    R.color.colorTxt)))

            holder.binding.ivComment.setOnClickListener {
                post.commentList(item.postId,position)
            }

            holder.binding.ivLike.setOnClickListener {

                if (item.isLiked)
                    post.unlike(item.postId,position)
                else
                    post.like(item.postId,position)
            }

            holder.binding.ivMore.setOnClickListener {

                showMenu(holder.binding.ivMore,context,position,item.userId,item.postId)
            }

            holder.binding.tvName.text= "${item.userDetails.firstName} ${item.userDetails.lastName}"
            holder.binding.tvLikeCount.text=item.likeCount
            holder.binding.tvCommentCount.text=item.commentCount
            holder.binding.tvCaption.text=item.caption
            loadImage(context,item.userDetails.profilePic,holder.binding.ivMap)
            //loadImage(context,item.userDetails.mascotIcon,holder.binding.ivAvatar)


        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostListPagingAdapter.PostViewHolder {
        val view=  LayoutPostRecyclerItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(view)
    }

    inner class PostViewHolder(var binding: LayoutPostRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    object PostListDiffCallback : DiffUtil.ItemCallback<PostListModel.Data.Post>() {
        override fun areItemsTheSame(oldItem: PostListModel.Data.Post, newItem: PostListModel.Data.Post) =
            oldItem.postId == newItem.postId

        override fun areContentsTheSame(
            oldItem: PostListModel.Data.Post,
            newItem: PostListModel.Data.Post
        ) = oldItem == newItem

        override fun getChangePayload(oldItem: PostListModel.Data.Post, newItem: PostListModel.Data.Post): Any? {
            if (oldItem != newItem) {
                return newItem
            }

            return super.getChangePayload(oldItem, newItem)
        }
    }

    private fun showMenu(
        deletePost: ImageView,
        context: Context,
        position: Int,
        userId: String,
        postId: String
    ) {
        val popupMenu = PopupMenu(context, deletePost)

        popupMenu.menuInflater.inflate(R.menu.menu_items, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.blockUser->{
                    post.block(position,userId)

                }
                R.id.reportPost->{
                    post.report(postId,position)

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
        fun like(postId:String,position: Int)
        fun report(postId:String,position: Int)
        fun unlike(postId:String,position: Int)
        fun comment(postId:String,position: Int,text:String)
        fun commentList(postId:String,position: Int)
    }
}