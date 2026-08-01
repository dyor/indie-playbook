package com.indieplaybook.app.domain.model

data class AppStory(
    val id: String,
    val name: String,
    val oneLiner: String,
    val techStack: String,
    val originStory: String,
    val growthPlaybook: String,
    val iconUrl: String,
    val isBookmarked: Boolean,
    val downloads: String? = null,
    val revenue: String? = null,
    val publisher: String? = null,
    val releaseDate: String? = null,
    val category: String? = null
)
