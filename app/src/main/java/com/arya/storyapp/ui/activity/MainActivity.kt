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
import com.arya.storyapp.local.DataStoreManager
import com.arya.storyapp.ui.adapter.ListStoryAdapter
import com.arya.storyapp.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels()
    private val dataStoreManager: DataStoreManager by lazy { DataStoreManager(this) }

    private val storiesAdapter = ListStoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setupUI()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logoutAndNavigateToLogin()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupUI() {
        binding.btnAddStory.setOnClickListener { navigateToAddStory() }
        binding.btnMaps.setOnClickListener { navigateToMaps() }
        setupRecyclerView()
        observeTokenAndLoadStories()
    }

    private fun setupRecyclerView() {
        binding.rvStories.layoutManager = LinearLayoutManager(this)
        binding.rvStories.adapter = storiesAdapter
    }

    private fun observeTokenAndLoadStories() = lifecycleScope.launch {
        dataStoreManager.tokenFlow.collectLatest { token ->
            if (token == null) navigateToLogin()
            else viewModel.getAllStories().observe(this@MainActivity) {
                storiesAdapter.submitData(lifecycle, it)
            }
        }
    }

    private fun logoutAndNavigateToLogin() {
        lifecycleScope.launch {
            dataStoreManager.clearToken()
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        finish()
    }

    private fun navigateToAddStory() {
        startActivity(Intent(this@MainActivity, AddStoryActivity::class.java))
    }

    private fun navigateToMaps() {
        startActivity(Intent(this@MainActivity, MapsActivity::class.java))
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }
}
