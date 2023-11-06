package com.arya.storyapp.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.arya.storyapp.R
import com.arya.storyapp.databinding.ActivityMapsBinding
import com.arya.storyapp.model.Story
import com.arya.storyapp.ui.viewmodel.MapsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val binding by lazy { ActivityMapsBinding.inflate(layoutInflater) }
    private lateinit var mMap: GoogleMap
    private val viewModel: MapsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.getStoriesLiveData().observe(this) { stories ->
            displayStoriesOnMap(stories)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        viewModel.fetchStoriesWithLocation()
    }

    private fun displayStoriesOnMap(stories: List<Story>) {
        if (stories.isNotEmpty()) {
            val mostRecentStory = stories[0]

            val latLng = LatLng(mostRecentStory.lat, mostRecentStory.lon)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
        }

        for (story in stories) {
            val latLng = LatLng(story.lat, story.lon)
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(story.name)
                    .snippet(story.description)
            )
        }
    }
}
