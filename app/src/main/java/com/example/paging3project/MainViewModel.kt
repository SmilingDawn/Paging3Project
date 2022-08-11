package com.example.paging3project

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.paging3project.model.RepositoryModel
import com.example.paging3project.repository.GitRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class MainViewModel(private val repository: GitRepository): ViewModel() {
    private val _searchKeyword: MutableLiveData<String> = MutableLiveData("android") // Git Search api는 "q=" 파라미터 값이 공백일경우 422 ErrorCode를 반환

    private val _inputKeyword: MutableLiveData<String> = MutableLiveData("")
    val inputKeyword: LiveData<String> get() = _inputKeyword

    private var searchKeywordJob: Job? = null

    var searchGitRep = _searchKeyword.switchMap {
        repository.getGitRepository(it).cachedIn(viewModelScope).asLiveData()
    }

    fun defaultSearch(): Flow<PagingData<RepositoryModel>> {
        return repository.getGitRepository("JavaScript").cachedIn(viewModelScope)
    }

    fun searchKeyword() {
        searchKeywordJob?.cancel()
        _searchKeyword.value = _inputKeyword.value
    }

    /**
     * Binding Method
     */
    fun onSearchKeywordTextChanged(sequence: CharSequence, start: Int, before: Int, count: Int) {
        val keyword = sequence.toString()
        _inputKeyword.value = keyword

        searchKeywordJob?.cancel()
        searchKeywordJob = CoroutineScope(Dispatchers.IO).launch {
            delay(3000)
            _searchKeyword.postValue(keyword)
        }
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