package com.indieplaybook.app.presentation.screens.homefeed

import com.indieplaybook.app.domain.model.AppStory

data class HomeFeedUiState(
    val filterOptions: List<String> = listOf("All", "Kotlin Multiplatform", "Flutter", "React Native"),
    val selectedFilter: String = "All",
    val stories: List<AppStory> = emptyList(),
)

sealed interface HomeFeedUiEvent {
    data class OnFilterSelected(val filter: String) : HomeFeedUiEvent
    data class OnStoryClicked(val story: AppStory) : HomeFeedUiEvent
    data class OnBookmarkClicked(val story: AppStory) : HomeFeedUiEvent
}
