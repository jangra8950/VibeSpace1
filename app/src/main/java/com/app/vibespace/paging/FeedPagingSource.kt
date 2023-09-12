package com.app.vibespace.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.app.vibespace.models.profile.PostListModel
import com.app.vibespace.service.MyRepo
import retrofit2.HttpException
import java.io.IOException

class FeedPagingSource(
    private val repo: MyRepo, val value:String
): PagingSource<Int, PostListModel.Data.Post>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostListModel.Data.Post> {
        val pageIndex = params.key ?: STARTING_PAGE_INDEX

        return try {
            val query: HashMap<String, Any> = hashMapOf()
            query["isSelf"] = false
            query["post"] = value
            query["limit"]=20
            query["offset"]=pageIndex

            val response=repo.getPostList(query)

            val nextKey =
                if (response.data.posts.isNullOrEmpty()) {
                    null
                } else {
                    pageIndex + 20
                }
            LoadResult.Page(
                data =  response.data.posts,
                prevKey = if (pageIndex == STARTING_PAGE_INDEX) null else pageIndex,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, PostListModel.Data.Post>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
    companion object {
        private const val STARTING_PAGE_INDEX = 0
    }
}