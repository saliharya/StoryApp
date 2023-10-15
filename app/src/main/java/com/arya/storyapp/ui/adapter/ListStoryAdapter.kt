package com.arya.storyapp.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arya.storyapp.R
import com.arya.storyapp.model.Story
import com.arya.storyapp.ui.activity.StoryDetailActivity
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
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, StoryDetailActivity::class.java)
            intent.putExtra("story", story)
            holder.itemView.context.startActivity(intent)
        }
    }

    class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val storyImageView: ImageView = itemView.findViewById(R.id.ivStory)
        private val usernameTextView: TextView = itemView.findViewById(R.id.tvUsername)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.tvDescription)

        fun bind(story: Story) {
            with(story) {
                usernameTextView.text = name
                descriptionTextView.text = description

                Glide.with(itemView)
                    .load(photoUrl)
                    .centerCrop()
                    .placeholder(R.color.black)
                    .into(storyImageView)
            }
        }
    }
}