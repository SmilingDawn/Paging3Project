package com.example.paging3project.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.paging3project.databinding.ItemListPhotoBinding
import com.example.paging3project.model.RepositoryModel

class GitRepoListAdapter: PagingDataAdapter<RepositoryModel, GitRepoListAdapter.GitRepoViewHolder>(
    object : DiffUtil.ItemCallback<RepositoryModel>() {
        override fun areItemsTheSame(oldItem: RepositoryModel, newItem: RepositoryModel): Boolean {
            return oldItem.id ==  newItem.id
        }

        override fun areContentsTheSame(oldItem: RepositoryModel, newItem: RepositoryModel): Boolean {
            return oldItem.id ==  newItem.id && oldItem == newItem
        }
    }
) {
    override fun onBindViewHolder(holder: GitRepoViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitRepoViewHolder {
        return GitRepoViewHolder(binding = ItemListPhotoBinding.inflate(LayoutInflater.from(parent.context)))
    }

    class GitRepoViewHolder(private val binding: ItemListPhotoBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: RepositoryModel?) {
            binding.tvTitle.text = item?.full_name
            Glide.with(binding.root.context).load(item?.owner?.avatar_url).into(binding.ivPhoto)
        }
    }
}