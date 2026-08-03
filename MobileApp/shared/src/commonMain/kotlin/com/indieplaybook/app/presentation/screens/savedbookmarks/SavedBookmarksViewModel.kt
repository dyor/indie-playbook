package com.indieplaybook.app.presentation.screens.savedbookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indieplaybook.app.data.repository.AppStoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SavedBookmarksViewModel(
    private val appStoryRepository: AppStoryRepository,
) : ViewModel() {

    val uiState: StateFlow<SavedBookmarksUiState> = appStoryRepository.getAllStories()
        .map { stories ->
            SavedBookmarksUiState(
                bookmarkedStories = stories.filter { it.isBookmarked },
            )
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, SavedBookmarksUiState())

    fun onUiEvent(event: SavedBookmarksUiEvent) {
        when (event) {
            is SavedBookmarksUiEvent.OnStoryClicked -> {
                // Navigated from UI directly
            }

            is SavedBookmarksUiEvent.OnBookmarkClicked -> {
                viewModelScope.launch {
                    appStoryRepository.toggleBookmark(event.story.id)
                }
            }
        }
    }
}
