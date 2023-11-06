package com.arya.storyapp.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arya.storyapp.R
import com.arya.storyapp.model.Story
import com.arya.storyapp.ui.activity.StoryDetailActivity
import com.bumptech.glide.Glide

class ListStoryAdapter :
    PagingDataAdapter<Story, ListStoryAdapter.StoryViewHolder>(StoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        story?.let { holder.bind(it) }
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, StoryDetailActivity::class.java).apply {
                putExtra("story", story)
            }

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                holder.itemView.context as Activity,
                Pair(holder.storyImageView, "image"),
                Pair(holder.usernameTextView, "username"),
                Pair(holder.descriptionTextView, "description")
            )

            holder.itemView.context.startActivity(intent, options.toBundle())
        }
    }

    class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val storyImageView: ImageView = itemView.findViewById(R.id.ivStory)
        internal val usernameTextView: TextView = itemView.findViewById(R.id.tvUsername)
        internal val descriptionTextView: TextView = itemView.findViewById(R.id.tvDescription)

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
