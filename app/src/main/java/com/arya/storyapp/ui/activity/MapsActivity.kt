package com.arya.storyapp.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.arya.storyapp.R
import com.arya.storyapp.databinding.ActivityMapsBinding
import com.arya.storyapp.local.DataStoreManager
import com.arya.storyapp.remote.response.StoryResponse
import com.arya.storyapp.repository.StoryRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val binding by lazy { ActivityMapsBinding.inflate(layoutInflater) }
    private lateinit var mMap: GoogleMap

    @Inject
    lateinit var storyRepository: StoryRepository

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        with(mMap.uiSettings) {
            isZoomControlsEnabled = true
            isIndoorLevelPickerEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }

        loadStories()
    }

    private fun loadStories() {
        lifecycleScope.launch {
            val token = dataStoreManager.getToken()
            if (token.isNullOrBlank()) {
                return@launch
            }

            storyRepository.getAllStories(token, null, null, 1).enqueue(
                onSuccess = { storyResponse ->
                    handleStoryResponse(storyResponse)
                },
                onFailure = {
                }
            )
        }
    }

    private fun handleStoryResponse(response: StoryResponse) {
        val stories = response.listStory
        stories.forEach { story ->
            val location = LatLng(story.lat, story.lon)
            mMap.addMarker(MarkerOptions().position(location).title(story.name))
        }
        if (stories.isNotEmpty()) {
            mMap.moveCamera(
                CameraUpdateFactory.newLatLng(
                    LatLng(
                        stories[0].lat,
                        stories[0].lon
                    )
                )
            )
        }
    }

    private inline fun <T> Call<T>.enqueue(
        crossinline onSuccess: (response: T) -> Unit,
        crossinline onFailure: () -> Unit
    ) {
        enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    onSuccess(response.body()!!)
                } else {
                    onFailure()
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                onFailure()
            }
        })
    }
}
