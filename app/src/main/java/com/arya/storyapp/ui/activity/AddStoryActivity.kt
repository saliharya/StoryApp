package com.arya.storyapp.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.arya.storyapp.databinding.ActivityAddStoryBinding
import com.arya.storyapp.ui.viewmodel.AddStoryViewModel
import com.arya.storyapp.util.getImageUri
import com.arya.storyapp.util.uriToFile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddStoryActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAddStoryBinding.inflate(layoutInflater) }
    private val viewModel: AddStoryViewModel by viewModels()

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnUpload.setOnClickListener { uploadImage() }

        // Observe successLiveData and navigate to the main activity on success
        viewModel.successLiveData.observe(this) { success ->
            if (success) {
                showToast("Story uploaded successfully")
                navigateToMainActivity()
            }
        }
    }

    private fun uploadImage() {
        val description = binding.etDescription.text.toString()
        if (description.isBlank()) {
            showToast("Please enter a description")
            return
        }

        val lat = 0.0f // Replace with the actual latitude
        val lon = 0.0f // Replace with the actual longitude

        currentImageUri?.let { uri ->
            val file = uriToFile(uri, this)

            // Call the view model to upload the story
            viewModel.addStory(description, file, lat, lon)

            // Show loading indicator if needed
            showLoading(true)
        } ?: showToast("Please select an image")
    }

    private fun showLoading(isLoading: Boolean) {
        // You can add loading indicator code here
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivUpload.setImageURI(it)
        }
    }

    private fun navigateToMainActivity() {
        // Create an intent to go back to the main activity
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

        // Start the main activity and finish this activity
        startActivity(intent)
        finish()
    }
}
