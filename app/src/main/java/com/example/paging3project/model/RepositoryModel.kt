package com.example.paging3project.model

import com.google.gson.annotations.SerializedName

data class RepositoryResponse(
    @SerializedName("total_count") val total_count: Int?,
    @SerializedName("incomplete_results") val incomplete_results: Boolean?,
    @SerializedName("items") val items: ArrayList<RepositoryModel>,
)

data class RepositoryModel(
    @SerializedName("id") val id: Int?,
    @SerializedName("full_name") val full_name: String?,
    @SerializedName("owner") val owner: OwnerModel?
)

data class OwnerModel(
    @SerializedName("id") val id: Int?,
    @SerializedName("avatar_url") val avatar_url: String?,
)
