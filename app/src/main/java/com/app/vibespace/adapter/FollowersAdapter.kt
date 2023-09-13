package com.app.vibespace.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.vibespace.databinding.LayoutFollowersListBinding

class FollowersAdapter(): RecyclerView.Adapter<FollowersAdapter.ViewHolder>()  {

    inner class ViewHolder(var binding: LayoutFollowersListBinding): RecyclerView.ViewHolder(binding.root){

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersAdapter.ViewHolder {
        val view=  LayoutFollowersListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowersAdapter.ViewHolder, position: Int) {
        holder.binding.tvName.text="sahil"
    }

    override fun getItemCount(): Int {
       return 10
    }


}