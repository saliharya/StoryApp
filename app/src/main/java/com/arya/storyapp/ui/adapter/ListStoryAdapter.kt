package com.arya.storyapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arya.storyapp.R
import com.arya.storyapp.model.Story
import com.bumptech.glide.Glide

class ListStoryAdapter :
    ListAdapter<Story, ListStoryAdapter.StoryViewHolder>(StoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)
    }

    class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val storyImageView = itemView.findViewById<ImageView>(R.id.ivStory)
        private val usernameTextView = itemView.findViewById<TextView>(R.id.tvUsername)
        private val descriptionTextView = itemView.findViewById<TextView>(R.id.tvDescription)

        fun bind(story: Story) {
            // Set the values for the views
            usernameTextView.text = story.name
            descriptionTextView.text = story.description

            // Load the image using Glide
            Glide.with(itemView)
                .load(story.photoUrl)
                .centerCrop()
                .placeholder(R.color.black) // Placeholder color while loading
                .into(storyImageView)
        }
    }
}

