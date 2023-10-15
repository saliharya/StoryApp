package com.arya.storyapp.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.arya.storyapp.model.Story

class StoryDiffCallback : DiffUtil.ItemCallback<Story>() {
    override fun areItemsTheSame(oldItem: Story, newItem: Story) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Story, newItem: Story) = oldItem == newItem
}