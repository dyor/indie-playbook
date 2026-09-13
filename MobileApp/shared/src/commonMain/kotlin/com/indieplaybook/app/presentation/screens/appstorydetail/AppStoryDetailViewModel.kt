package com.indieplaybook.app.presentation.screens.appstorydetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indieplaybook.app.data.repository.AppStoryRepository
import com.indieplaybook.app.data.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class AppStoryDetailViewModel(
    private val storyId: String,
    private val appStoryRepository: AppStoryRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val storyIdFlow = MutableStateFlow(storyId)

    val uiState: StateFlow<AppStoryDetailUiState> = combine(
        storyIdFlow.flatMapLatest { id ->
            if (id.isEmpty()) flowOf(null) else appStoryRepository.getStoryById(id)
        },
        userRepository.currentUser,
    ) { story, userResult ->
        val user = userResult.getOrNull()
        AppStoryDetailUiState(
            storyId = storyId,
            story = story,
            isAdmin = user?.isAdmin == true,
        )
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
