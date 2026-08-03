package com.indieplaybook.app.presentation.screens.appstorydetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indieplaybook.app.data.repository.AppStoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class AppStoryDetailViewModel(
    private val storyId: String,
    private val appStoryRepository: AppStoryRepository,
) : ViewModel() {

    private val storyIdFlow = MutableStateFlow(storyId)

    val uiState: StateFlow<AppStoryDetailUiState> = storyIdFlow.flatMapLatest { id ->
        if (id.isEmpty()) {
            flowOf(AppStoryDetailUiState())
        } else {
            appStoryRepository.getStoryById(id).map { story ->
                AppStoryDetailUiState(
                    storyId = id,
                    story = story,
                )
            }
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, AppStoryDetailUiState(storyId = storyId))

    fun onUiEvent(event: AppStoryDetailUiEvent) {
        when (event) {
            is AppStoryDetailUiEvent.OnBookmarkClicked -> {
                viewModelScope.launch {
                    appStoryRepository.toggleBookmark(storyId)
                }
            }
        }
    }
}
