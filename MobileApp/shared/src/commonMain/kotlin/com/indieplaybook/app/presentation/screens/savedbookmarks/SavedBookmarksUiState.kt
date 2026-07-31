package com.indieplaybook.app.presentation.screens.savedbookmarks

import com.indieplaybook.app.domain.model.AppStory

data class SavedBookmarksUiState(
    val bookmarkedStories: List<AppStory> = emptyList(),
)

sealed interface SavedBookmarksUiEvent {
    data class OnStoryClicked(val story: AppStory) : SavedBookmarksUiEvent
    data class OnBookmarkClicked(val story: AppStory) : SavedBookmarksUiEvent
}
