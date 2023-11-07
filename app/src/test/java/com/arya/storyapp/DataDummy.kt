package com.arya.storyapp

import com.arya.storyapp.model.Story

object DataDummy {

    fun generateDummyStoryResponse(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                "", "", "", "", "", 0.0, 0.0
            )
            items.add(story)
        }
        return items
    }
}