Paging3Project
========================

## Introduction
Android [Paging3][1] 를 이용한 프로젝트

## Descrption
github에서 제공해주는 search api를 이용하여 paging이 되는 리스트를 구현

```kotlin
object RetrofitInstance {

    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
```
```kotlin
interface RepositoryAPI {
    @GET("/search/repositories")
    suspend fun getRepositories(@QueryMap params: MutableMap<String, String?>): RepositoryResponse?
}
```

새로운 검색어를 입력할때마다 RecyclerView가 새로 갱신되도록 함
```kotlin
class MainViewModel(private val repository: GitRepository): ViewModel() {
    private val _searchKeyword: MutableLiveData<String> = MutableLiveData("")
    private val searchKeyword: LiveData<String> get() = _searchKeyword

    fun setSearchGitRepo(keyword: String) {
        _searchKeyword.value = keyword
    }

    var searchGitRep = searchKeyword.switchMap {
        repository.getGitRepository(it).cachedIn(viewModelScope).asLiveData()
    }
}
```

[1]: https://developer.android.com/topic/libraries/architecture/paging/data?hl=ko
