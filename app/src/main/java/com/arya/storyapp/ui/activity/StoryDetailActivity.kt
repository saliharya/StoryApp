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
import com.arya.storyapp.local.DataStoreManager
import com.arya.storyapp.model.Story
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding
    private val dataStoreManager: DataStoreManager by lazy { DataStoreManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setupUI()
    }

    private fun setupUI() {
        binding.btnAddStory.setOnClickListener { navigateToAddStory() }

        val story: Story? = intent.getParcelableExtra("story")
        story?.let { displayStoryDetails(it) }
    }

    private fun displayStoryDetails(story: Story) {
        Glide.with(this)
            .load(story.photoUrl)
            .centerCrop()
            .placeholder(R.color.black)
            .into(binding.ivStory)
        binding.ivStory.transitionName = "image"

        binding.tvUsername.text = story.name
        binding.tvUsername.transitionName = "username"

        binding.tvDescription.text = story.description
        binding.tvDescription.transitionName = "description"
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

    private fun logoutAndNavigateToLogin() {
        lifecycleScope.launch {
            dataStoreManager.clearToken()
            navigateToLogin()
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
