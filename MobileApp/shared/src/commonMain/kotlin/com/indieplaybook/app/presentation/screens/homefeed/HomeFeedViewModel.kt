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
import kotlinx.coroutines.launch

class HomeFeedViewModel(
    private val appStoryRepository: AppStoryRepository,
) : ViewModel() {

    private val selectedFrameworkFilterFlow = MutableStateFlow("All")
    private val selectedStoreFilterFlow = MutableStateFlow("All Stores")
    private val selectedStoryFilterFlow = MutableStateFlow("All Stories")

    val uiState: StateFlow<HomeFeedUiState> = combine(
        appStoryRepository.getAllStories(),
        selectedFrameworkFilterFlow,
        selectedStoreFilterFlow,
        selectedStoryFilterFlow,
    ) { stories, frameworkFilter, storeFilter, storyFilter ->
        val filteredStories = stories.filter { story ->
            val matchesFramework = when (frameworkFilter) {
                "All" -> true
                else -> story.techStack.equals(frameworkFilter, ignoreCase = true)
            }

            val matchesStore = when (storeFilter) {
                "All Stores" -> true
                "Dual Store" -> story.storePresence == "Dual Store" || (!story.googlePlayUrl.isNullOrBlank() && !story.appStoreUrl.isNullOrBlank())
                "App Store Only" -> story.storePresence == "App Store Only" || (story.googlePlayUrl.isNullOrBlank() && !story.appStoreUrl.isNullOrBlank())
                else -> true
            }

            val matchesStory = when (storyFilter) {
                "All Stories" -> true
                "Origin Story" -> story.originStory.isNotBlank()
                "Growth Story" -> story.growthPlaybook.isNotBlank()
                else -> true
            }

            matchesFramework && matchesStore && matchesStory
        }

        HomeFeedUiState(
            selectedFrameworkFilter = frameworkFilter,
            selectedStoreFilter = storeFilter,
            selectedStoryFilter = storyFilter,
            stories = filteredStories,
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, HomeFeedUiState())

    init {
        viewModelScope.launch {
            appStoryRepository.seedInitialDataIfEmpty()
        }
    }

    fun onUiEvent(event: HomeFeedUiEvent) {
        when (event) {
            is HomeFeedUiEvent.OnFrameworkFilterSelected -> {
                selectedFrameworkFilterFlow.value = event.filter
            }

            is HomeFeedUiEvent.OnStoreFilterSelected -> {
                selectedStoreFilterFlow.value = event.filter
            }

            is HomeFeedUiEvent.OnStoryFilterSelected -> {
                selectedStoryFilterFlow.value = event.filter
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
