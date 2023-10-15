package com.arya.storyapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.arya.storyapp.R
import com.arya.storyapp.databinding.ActivityStoryDetailBinding
import com.arya.storyapp.model.Story
import com.arya.storyapp.ui.viewmodel.StoryDetailViewModel
import com.arya.storyapp.util.DataStoreManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding
    private val viewModel: StoryDetailViewModel by viewModels()
    private val dataStoreManager: DataStoreManager by lazy {
        DataStoreManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.btnAddStory.setOnClickListener {
            navigateToAddStory()
        }

        val story: Story? = intent.getParcelableExtra("story")

        if (story != null) {
            Glide.with(this)
                .load(story.photoUrl)
                .centerCrop()
                .placeholder(R.color.black)
                .into(binding.ivStory)

            binding.tvUsername.text = story.name
            binding.tvDescription.text = story.description
        }

        viewModel.isLoadingLiveData.observe(this) { isLoading ->
            if (isLoading) {
            } else {
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
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

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun navigateToAddStory() {
        startActivity(Intent(this, AddStoryActivity::class.java))
    }
}
