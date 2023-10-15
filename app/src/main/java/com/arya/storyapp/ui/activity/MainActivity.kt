package com.arya.storyapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.arya.storyapp.databinding.ActivityMainBinding
import com.arya.storyapp.ui.adapter.ListStoryAdapter
import com.arya.storyapp.ui.viewmodel.MainViewModel
import com.arya.storyapp.util.DataStoreManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    private val storiesAdapter = ListStoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()
        observeTokenAndLoadStories()
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
            // Handle loading indicator if needed
        }
    }

    private fun observeTokenAndLoadStories() = lifecycleScope.launch {
        dataStoreManager.tokenFlow.collectLatest { token ->
            if (token == null) navigateToLogin()
            else viewModel.getAllStories(token).also { setupLogoutButton() }
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        finish()
    }

    private fun setupLogoutButton() = binding.btnLogout.setOnClickListener {
        lifecycleScope.launch {
            dataStoreManager.clearToken()
            navigateToLogin()
        }
    }
}
