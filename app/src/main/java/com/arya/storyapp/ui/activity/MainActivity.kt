package com.arya.storyapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.arya.storyapp.repository.StoryRepository
import com.arya.storyapp.api.StoryService
import com.arya.storyapp.databinding.ActivityMainBinding
import com.arya.storyapp.ui.adapter.ListStoriesAdapter
import com.arya.storyapp.ui.viewmodel.MainViewModel
import com.arya.storyapp.ui.viewmodel.MainViewModelFactory
import com.arya.storyapp.util.DataStoreManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var dataStoreManager: DataStoreManager
    private val storiesAdapter = ListStoriesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val storyService = retrofit.create(StoryService::class.java)
        val storyRepository = StoryRepository(storyService)
        dataStoreManager = DataStoreManager(this)
        val viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(storyRepository)
        )[MainViewModel::class.java]

        // Set up RecyclerView and adapter
        binding.rvStories.layoutManager = LinearLayoutManager(this)
        binding.rvStories.adapter = storiesAdapter

        // Observe LiveData in the ViewModel
        viewModel.responseLiveData.observe(this) { stories ->
            storiesAdapter.submitList(stories)
        }

        viewModel.errorLiveData.observe(this) { error ->
            // Handle error, e.g., show a message to the user
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }

        viewModel.isLoadingLiveData.observe(this) { isLoading ->
            if (isLoading) {
                // Show a loading indicator
                //binding.progressBar.visibility = View.VISIBLE
            } else {
                // Hide the loading indicator
                //binding.progressBar.visibility = View.GONE
            }
        }

        // Observe the token and load stories if the token is available
        lifecycleScope.launch {
            dataStoreManager.tokenFlow.collectLatest { token ->
                if (token == null) {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Load stories when the token is available
                    viewModel.getAllStories(token)
                    binding.btnLogout.setOnClickListener {
                        lifecycleScope.launch {
                            dataStoreManager.clearToken()
                            val intent = Intent(this@MainActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }
    }
}
