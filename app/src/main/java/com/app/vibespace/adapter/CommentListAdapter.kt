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
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.vibespace.R
import com.app.vibespace.databinding.LayoutCommentItemBinding
import com.app.vibespace.databinding.LayoutPostRecyclerItemBinding
import com.app.vibespace.models.profile.PostCommentListModel
import com.app.vibespace.models.profile.PostListModel

class CommentListAdapter(private val mList:ArrayList<PostCommentListModel.Data.Comment>,
                          var context: Context
): RecyclerView.Adapter<CommentListAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: LayoutCommentItemBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=  LayoutCommentItemBinding
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

    }




}