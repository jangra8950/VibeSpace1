package com.app.vibespace.adapter

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.vibespace.R
import com.app.vibespace.databinding.LayoutPostRecyclerItemBinding
import com.app.vibespace.models.profile.PostListModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.NonDisposableHandle.parent


class PostAllAdapter(private val mList:ArrayList<PostListModel.Data.Post>,
                    private val post: Post,var context: Context
): RecyclerView.Adapter<PostAllAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: LayoutPostRecyclerItemBinding): RecyclerView.ViewHolder(binding.root){
//        init {
//            itemView.setOnClickListener {
//               binding.tvTypeComment.clearFocus()
//                val imm =
//                    context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
//                imm.hideSoftInputFromWindow( binding.tvTypeComment.windowToken, 0)
//            }
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=  LayoutPostRecyclerItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position>=0)
            holder.binding.data=mList[position]
        val item=mList[position]
        if (item.isLiked)
            ImageViewCompat.setImageTintList(holder.binding.ivLike, ColorStateList.valueOf(context.getColor(R.color.colorRed)))
        else
            ImageViewCompat.setImageTintList(holder.binding.ivLike, ColorStateList.valueOf(context.getColor(R.color.colorTxt)))
        holder.binding.ivMore.setOnClickListener {

            showMenu(holder.binding.ivMore,context,position,item.userId,item.postId)
        }

//        holder.binding.tvTypeComment.addTextChangedListener(object : TextWatcher{
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                if (p0.toString().trim()!="")
//                    holder.binding.ivSend.visibility=View.VISIBLE
//                else
//                    holder.binding.ivSend.visibility=View.GONE
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//
//            }
//
//        })

        holder.binding.ivLike.setOnClickListener {

            if (item.isLiked)
                post.unlike(item.postId,position)
            else
                post.like(item.postId,position)
        }

//        holder.binding.ivSend.setOnClickListener {
//            holder.binding.tvTypeComment.clearFocus()
//            val imm =
//                context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(holder.binding.tvTypeComment.windowToken, 0)
//              post.comment(item.postId,position,holder.binding.tvTypeComment.text.toString())
//           // holder.binding.tvTypeComment.setText("")
//        }

        holder.binding.ivComment.setOnClickListener {
            post.commentList(item.postId,position)
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