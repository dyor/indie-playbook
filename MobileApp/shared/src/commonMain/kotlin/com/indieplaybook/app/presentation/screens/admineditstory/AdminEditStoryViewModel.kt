package com.indieplaybook.app.presentation.screens.admineditstory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indieplaybook.app.data.repository.AppStoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminEditStoryViewModel(
    private val storyId: String,
    private val appStoryRepository: AppStoryRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminEditStoryUiState(storyId = storyId))
    val uiState: StateFlow<AdminEditStoryUiState> = _uiState.asStateFlow()

    init {
        loadStory()
    }

    private fun loadStory() {
        viewModelScope.launch {
            val story = appStoryRepository.getStoryById(storyId).first()
            if (story != null) {
                _uiState.update {
                    it.copy(
                        originalStory = story,
                        name = story.name,
                        oneLiner = story.oneLiner,
                        techStack = story.techStack,
                        originStory = story.originStory,
                        growthPlaybook = story.growthPlaybook,
                        category = story.category ?: "",
                        publisher = story.publisher ?: "",
                        revenue = story.revenue ?: "",
                        downloads = story.downloads ?: "",
                        isLoading = false,
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Story not found.") }
            }
        }
    }

    fun onUiEvent(event: AdminEditStoryUiEvent) {
        when (event) {
            is AdminEditStoryUiEvent.OnNameChanged -> _uiState.update { it.copy(name = event.value) }
            is AdminEditStoryUiEvent.OnOneLinerChanged -> _uiState.update { it.copy(oneLiner = event.value) }
            is AdminEditStoryUiEvent.OnTechStackChanged -> _uiState.update { it.copy(techStack = event.value) }
            is AdminEditStoryUiEvent.OnOriginStoryChanged -> _uiState.update { it.copy(originStory = event.value) }
            is AdminEditStoryUiEvent.OnGrowthPlaybookChanged -> _uiState.update { it.copy(growthPlaybook = event.value) }
            is AdminEditStoryUiEvent.OnCategoryChanged -> _uiState.update { it.copy(category = event.value) }
            is AdminEditStoryUiEvent.OnPublisherChanged -> _uiState.update { it.copy(publisher = event.value) }
            is AdminEditStoryUiEvent.OnRevenueChanged -> _uiState.update { it.copy(revenue = event.value) }
            is AdminEditStoryUiEvent.OnDownloadsChanged -> _uiState.update { it.copy(downloads = event.value) }
            AdminEditStoryUiEvent.OnSaveClicked -> saveDirectEdit()
        }
    }

    private fun saveDirectEdit() {
        val current = _uiState.value
        val original = current.originalStory ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            val updated = original.copy(
                name = current.name,
                oneLiner = current.oneLiner,
                techStack = current.techStack,
                originStory = current.originStory,
                growthPlaybook = current.growthPlaybook,
                category = current.category,
                publisher = current.publisher,
                revenue = current.revenue,
                downloads = current.downloads,
            )

            val result = appStoryRepository.updateStoryDirectly(updated)
            if (result.isSuccess) {
                _uiState.update { it.copy(isSaving = false, isSavedSuccessfully = true) }
            } else {
                _uiState.update { it.copy(isSaving = false, errorMessage = "Failed to update story.") }
            }
        }
    }
}
