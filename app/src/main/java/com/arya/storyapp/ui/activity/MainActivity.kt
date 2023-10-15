package com.arya.storyapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.arya.storyapp.R
import com.arya.storyapp.databinding.ActivityMainBinding
import com.arya.storyapp.ui.adapter.ListStoryAdapter
import com.arya.storyapp.ui.viewmodel.MainViewModel
import com.arya.storyapp.util.DataStoreManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels()
    private val dataStoreManager: DataStoreManager by lazy {
        DataStoreManager(this)
    }

    private val storiesAdapter = ListStoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.btnAddStory.setOnClickListener {
            navigateToAddStory()
        }

        setupRecyclerView()
        observeViewModel()
        observeTokenAndLoadStories()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                lifecycleScope.launch {
                    dataStoreManager.clearToken()
                    navigateToLogin()
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView() = with(binding.rvStories) {
        layoutManager = LinearLayoutManager(this@MainActivity)
        adapter = storiesAdapter
    }

    private fun observeViewModel() = with(viewModel) {
        responseLiveData.observe(this@MainActivity) { stories ->
            storiesAdapter.submitList(stories)
        }

        errorLiveData.observe(this@MainActivity) { error ->
            Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
        }

        isLoadingLiveData.observe(this@MainActivity) {
        }
    }

    private fun observeTokenAndLoadStories() = lifecycleScope.launch {
        dataStoreManager.tokenFlow.collectLatest { token ->
            if (token == null) navigateToLogin()
            else viewModel.getAllStories(token)
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        finish()
    }

    private fun navigateToAddStory() {
        startActivity(Intent(this@MainActivity, AddStoryActivity::class.java))
    }
}
