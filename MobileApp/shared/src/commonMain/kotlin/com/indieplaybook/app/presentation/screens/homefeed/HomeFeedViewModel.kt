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

private data class FilterState(
    val framework: String = "All",
    val store: String = "All Stores",
    val story: String = "All Stories",
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
)

class HomeFeedViewModel(
    private val appStoryRepository: AppStoryRepository,
) : ViewModel() {

    private val filterStateFlow = MutableStateFlow(FilterState())

    val uiState: StateFlow<HomeFeedUiState> = combine(
        appStoryRepository.getAllStories(),
        filterStateFlow,
    ) { stories, filters ->
        val filteredStories = stories.filter { story ->
            val matchesFramework = when (filters.framework) {
                "All" -> true
                else -> story.techStack.equals(filters.framework, ignoreCase = true)
            }

            val matchesStore = when (filters.store) {
                "All Stores" -> true
                "Dual Store" -> story.storePresence == "Dual Store" || (!story.googlePlayUrl.isNullOrBlank() && !story.appStoreUrl.isNullOrBlank())
                "App Store Only" -> story.storePresence == "App Store Only" || (story.googlePlayUrl.isNullOrBlank() && !story.appStoreUrl.isNullOrBlank())
                else -> true
            }

            val matchesStory = when (filters.story) {
                "All Stories" -> true
                "Origin Story" -> story.originStory.isNotBlank()
                "Growth Story" -> story.growthPlaybook.isNotBlank()
                else -> true
            }

            matchesFramework && matchesStore && matchesStory
        }

        HomeFeedUiState(
            selectedFrameworkFilter = filters.framework,
            selectedStoreFilter = filters.store,
            selectedStoryFilter = filters.story,
            stories = filteredStories,
            isLoading = filters.isLoading,
            errorMessage = filters.errorMessage,
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, HomeFeedUiState())

    init {
        fetchStories()
    }

    private fun fetchStories() {
        viewModelScope.launch {
            filterStateFlow.update { it.copy(isLoading = true, errorMessage = null) }
            val result = appStoryRepository.seedInitialDataIfEmpty()
            if (result.isFailure) {
                filterStateFlow.update { it.copy(isLoading = false, errorMessage = "Cannot connect to Firestore") }
            } else {
                filterStateFlow.update { it.copy(isLoading = false, errorMessage = null) }
            }
        }
    }

    fun onUiEvent(event: HomeFeedUiEvent) {
        when (event) {
            is HomeFeedUiEvent.OnFrameworkFilterSelected -> {
                filterStateFlow.update { it.copy(framework = event.filter) }
            }

            is HomeFeedUiEvent.OnStoreFilterSelected -> {
                filterStateFlow.update { it.copy(store = event.filter) }
            }

            is HomeFeedUiEvent.OnStoryFilterSelected -> {
                filterStateFlow.update { it.copy(story = event.filter) }
            }

            is HomeFeedUiEvent.OnStoryClicked -> {
                // Navigated from UI directly
            }

            is HomeFeedUiEvent.OnBookmarkClicked -> {
                viewModelScope.launch {
                    appStoryRepository.toggleBookmark(event.story.id)
                }
            }

            HomeFeedUiEvent.OnRetryClicked -> {
                fetchStories()
            }
        }
    }
}
