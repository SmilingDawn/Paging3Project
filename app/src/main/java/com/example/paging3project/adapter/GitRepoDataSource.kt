package com.example.paging3project.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.paging3project.api.RepositoryAPI
import com.example.paging3project.model.RepositoryModel
import java.lang.Exception

class GitRepoDataSource(private val repositoryAPI: RepositoryAPI, private val search: String?, private val queryParams: MutableMap<String, String?>): PagingSource<Int, RepositoryModel>() {
    override fun getRefreshKey(state: PagingState<Int, RepositoryModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepositoryModel> {
        return try {
            val page = params.key ?: 1
            val limit = params.loadSize

            queryParams.run {
                this["page"] = page.toString()
                this["sort"] = "forks" // or starts
                this["q"] = search
            }

            val response = repositoryAPI.getRepositories(queryParams)
            val data = response?.items?.toList() ?: listOf()
            val total = response?.total_count
            val nextPage = if(data.count() == 20) page + 1 else null

            LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

}