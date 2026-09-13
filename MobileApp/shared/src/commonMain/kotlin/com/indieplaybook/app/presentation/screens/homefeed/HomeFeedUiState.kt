package com.indieplaybook.app.presentation.screens.homefeed

import com.indieplaybook.app.domain.model.AppStory

data class HomeFeedUiState(
    val frameworkFilterOptions: List<String> = listOf("All", "KMP", "Flutter", "React Native"),
    val selectedFrameworkFilter: String = "All",
    val storeFilterOptions: List<String> = listOf("All Stores", "Dual Store", "App Store Only"),
    val selectedStoreFilter: String = "All Stores",
    val storyFilterOptions: List<String> = listOf("All Stories", "Origin Story", "Growth Story"),
    val selectedStoryFilter: String = "All Stories",
    val stories: List<AppStory> = emptyList(),
)

sealed interface HomeFeedUiEvent {
    data class OnFrameworkFilterSelected(val filter: String) : HomeFeedUiEvent
    data class OnStoreFilterSelected(val filter: String) : HomeFeedUiEvent
    data class OnStoryFilterSelected(val filter: String) : HomeFeedUiEvent
    data class OnStoryClicked(val story: AppStory) : HomeFeedUiEvent
    data class OnBookmarkClicked(val story: AppStory) : HomeFeedUiEvent
}
