package com.indieplaybook.app.presentation.screens.homefeed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indieplaybook.app.data.repository.AppStoryRepository
import com.indieplaybook.app.domain.model.AppStory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeFeedViewModel(
    private val appStoryRepository: AppStoryRepository
) : ViewModel() {

    private val _selectedFilter = MutableStateFlow("All")

    val uiState: StateFlow<HomeFeedUiState> = combine(
        appStoryRepository.getAllStories(),
        _selectedFilter
    ) { stories, selectedFilter ->
        val filteredStories = if (selectedFilter == "All") {
            stories
        } else {
            stories.filter { it.techStack == selectedFilter }
        }
        HomeFeedUiState(
            selectedFilter = selectedFilter,
            stories = filteredStories
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, HomeFeedUiState())

    init {
        viewModelScope.launch {
            appStoryRepository.seedInitialDataIfEmpty()
        }
    }

    fun onUiEvent(event: HomeFeedUiEvent) {
        when (event) {
            is HomeFeedUiEvent.OnFilterSelected -> {
                _selectedFilter.value = event.filter
            }
            is HomeFeedUiEvent.OnStoryClicked -> {
                // Navigated from UI directly
            }
            is HomeFeedUiEvent.OnBookmarkClicked -> {
                viewModelScope.launch {
                    appStoryRepository.toggleBookmark(event.story.id)
                }
            }
        }
    }
}
