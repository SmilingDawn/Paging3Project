package com.example.paging3project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paging3project.adapter.GitRepoListAdapter
import com.example.paging3project.databinding.ActivityMainBinding
import com.example.paging3project.repository.GitRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
    }

    /**
     * Binding Method
     */
    fun onClickSearch1() {
        viewModel.setSearchGitRepo("rxSwift")
    }

    /**
     * Binding Method
     */
    fun onClickSearch2() {
        viewModel.setSearchGitRepo("Kotlin")
    }
}