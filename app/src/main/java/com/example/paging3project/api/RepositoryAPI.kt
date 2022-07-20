package com.example.paging3project.api

import com.example.paging3project.model.RepositoryResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RepositoryAPI {
    @GET("/search/repositories")
    suspend fun getRepositories(@QueryMap params: MutableMap<String, String?>): RepositoryResponse?
}