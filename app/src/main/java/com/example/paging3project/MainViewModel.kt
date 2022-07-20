package com.example.paging3project

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.paging3project.model.RepositoryModel
import com.example.paging3project.repository.GitRepository
import kotlinx.coroutines.flow.Flow

class MainViewModel(private val repository: GitRepository): ViewModel() {
    private val _searchKeyword: MutableLiveData<String> = MutableLiveData("")
    private val searchKeyword: LiveData<String> get() = _searchKeyword

    fun setSearchGitRepo(sort: String) {
        _searchKeyword.value = sort
    }

    var searchGitRep = searchKeyword.switchMap {
        repository.getGitRepository(it).cachedIn(viewModelScope).asLiveData()
    }

    fun defaultSearch(): Flow<PagingData<RepositoryModel>> {
        return repository.getGitRepository("JavaScript").cachedIn(viewModelScope)
    }

    class Factory(private val repository: GitRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(repository) as T
            }

            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}