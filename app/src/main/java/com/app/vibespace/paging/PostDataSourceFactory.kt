package com.app.vibespace.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.app.vibespace.service.MyRepo

class PostDataSourceFactory(private val dataRepository: MyRepo)
{
    fun getList()= Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { FeedPagingSource(dataRepository) }
    ).liveData
}