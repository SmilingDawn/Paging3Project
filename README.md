Paging3Project
========================

## Introduction
Android [Paging3][1] 를 이용한 프로젝트.   
기존 Infinity Scroll List 구현 방식은 addOnScrollListener 을 상속받아 스크롤이 제일 하단에 도착했을 때 다음 page를 로드하는 방식이었으나   
Paging3을 사용할 경우 현재 Item Count, Total Count, isLoading 등등을 신경쓰지 않고도 Infinity Scroll List를 구현할 수 있다.

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
### DataSource
```kotlin
class GitRepoDataSource(private val repositoryAPI: RepositoryAPI, private val search: String?, private val queryParams: MutableMap<String, String?>): PagingSource<Int, RepositoryModel>() {
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
```

[1]: https://developer.android.com/topic/libraries/architecture/paging/data?hl=ko
