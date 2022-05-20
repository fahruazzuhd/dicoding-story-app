package com.fahruaz.storyapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.fahruaz.storyapp.Response.ListStoryItem
import com.fahruaz.storyapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private var binding: ActivityDetailStoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val data = intent.getParcelableExtra<ListStoryItem>(EXTRA_STORY) as ListStoryItem

        Glide.with(this@DetailStoryActivity)
            .load(data.photoUrl)
            .into(binding?.imgItemPhoto!!)
        binding?.tvName?.text = data.name
        binding?.tvDescription?.text = data.description
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}