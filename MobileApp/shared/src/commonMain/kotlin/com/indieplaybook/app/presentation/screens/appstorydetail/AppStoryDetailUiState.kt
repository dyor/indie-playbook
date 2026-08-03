package com.indieplaybook.app.presentation.screens.appstorydetail

import com.indieplaybook.app.domain.model.AppStory

data class AppStoryDetailUiState(
    val storyId: String = "",
    val story: AppStory? = null,
)

sealed interface AppStoryDetailUiEvent {
    data object OnBookmarkClicked : AppStoryDetailUiEvent
}
