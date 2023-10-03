package com.app.vibespace.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.vibespace.databinding.LayoutLoaderBinding

class LoadAdapter :LoadStateAdapter<LoadAdapter.LoadViewHolder>() {

    inner class LoadViewHolder(var binding:LayoutLoaderBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onBindViewHolder(holder: LoadViewHolder, loadState: LoadState) {
       holder.binding.progressBar.isVisible= loadState is LoadState.Loading
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadViewHolder {
       val view=LayoutLoaderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LoadViewHolder(view)
    }

}