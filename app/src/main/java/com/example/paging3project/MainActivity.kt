package com.example.paging3project

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paging3project.adapter.GitRepoListAdapter
import com.example.paging3project.databinding.ActivityMainBinding
import com.example.paging3project.repository.GitRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val repository: GitRepository by lazy { GitRepository() }
    private val viewModelFactory = MainViewModel.Factory(repository)

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    private val adapter: GitRepoListAdapter by lazy { GitRepoListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.activity = this
        binding.viewModel = viewModel

        binding.rvView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            adapter = this@MainActivity.adapter
        }

//        lifecycleScope.launchWhenStarted {
//            viewModel.defaultSearch().collect() {
//                adapter.submitData(it)
//            }
//        }

        lifecycleScope.launchWhenStarted {
            viewModel.searchGitRep.observe(this@MainActivity) {
                CoroutineScope(Dispatchers.Main).launch {
                    adapter.submitData(it)
                }
            }
        }

        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.progressCircular.isVisible = loadStates.refresh is LoadState.Loading

                val error = when {
                    loadStates.prepend is LoadState.Error -> loadStates.prepend as LoadState.Error
                    loadStates.append is LoadState.Error -> loadStates.append as LoadState.Error
                    loadStates.refresh is LoadState.Error -> loadStates.refresh as LoadState.Error
                    else -> null
                }
                error?.let {
                    Toast.makeText(this@MainActivity, it.error.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**
     * Binding Method
     */
    fun onClickSearch() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.etSearchInput.windowToken, 0)
        viewModel.searchKeyword()
    }
}