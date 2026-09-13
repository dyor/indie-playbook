package com.indieplaybook.app.domain.model

data class AppStorySuggestion(
    val id: String = "",
    val storyId: String = "",
    val storyName: String = "",
    val userId: String = "",
    val userEmail: String? = null,
    val userDisplayName: String? = null,
    val timestamp: Long = 0L,
    val status: String = "pending", // "pending", "approved", "rejected"
    val suggestedName: String? = null,
    val suggestedOneLiner: String? = null,
    val suggestedTechStack: String? = null,
    val suggestedOriginStory: String? = null,
    val suggestedGrowthPlaybook: String? = null,
    val suggestedCategory: String? = null,
    val suggestedPublisher: String? = null,
    val suggestedRevenue: String? = null,
    val suggestedDownloads: String? = null,
    val note: String? = null,
)
