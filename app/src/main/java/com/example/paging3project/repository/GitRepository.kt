package com.example.paging3project.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.paging3project.adapter.GitRepoDataSource
import com.example.paging3project.api.RepositoryAPI
import com.example.paging3project.api.RetrofitInstance
import com.example.paging3project.model.RepositoryModel
import kotlinx.coroutines.flow.Flow

class GitRepository {
    fun getGitRepository(search: String): Flow<PagingData<RepositoryModel>> {
        val params = mutableMapOf<String, String?>().apply {
            this["per_page"] = "20"
        }

        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { GitRepoDataSource(RetrofitInstance.retrofit.create(RepositoryAPI::class.java), search, params) }
        ).flow
    }
}