@file:OptIn(ExperimentalTime::class)

package com.indieplaybook.app.presentation.screens.suggestedits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indieplaybook.app.data.repository.AppStoryRepository
import com.indieplaybook.app.data.repository.UserRepository
import com.indieplaybook.app.data.source.local.entity.cleanPlaceholder
import com.indieplaybook.app.domain.model.AppStorySuggestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class SuggestEditsViewModel(
    private val storyId: String,
    private val appStoryRepository: AppStoryRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SuggestEditsUiState(storyId = storyId))
    val uiState: StateFlow<SuggestEditsUiState> = _uiState.asStateFlow()

    init {
        loadStory()
    }

    private fun loadStory() {
        if (storyId == "new" || storyId.isBlank()) {
            _uiState.update {
                it.copy(
                    originalStory = null,
                    name = "",
                    oneLiner = "",
                    techStack = "",
                    originStory = "",
                    growthPlaybook = "",
                    category = "",
                    publisher = "",
                    revenue = "",
                    downloads = "",
                    isLoading = false,
                )
            }
            return
        }

        viewModelScope.launch {
            val story = appStoryRepository.getStoryById(storyId).first()
            if (story != null) {
                _uiState.update {
                    it.copy(
                        originalStory = story,
                        name = story.name,
                        oneLiner = story.oneLiner,
                        techStack = story.techStack,
                        originStory = cleanPlaceholder(story.originStory),
                        growthPlaybook = cleanPlaceholder(story.growthPlaybook),
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

    fun onUiEvent(event: SuggestEditsUiEvent) {
        when (event) {
            is SuggestEditsUiEvent.OnNameChanged -> _uiState.update { it.copy(name = event.value) }
            is SuggestEditsUiEvent.OnOneLinerChanged -> _uiState.update { it.copy(oneLiner = event.value) }
            is SuggestEditsUiEvent.OnTechStackChanged -> _uiState.update { it.copy(techStack = event.value) }
            is SuggestEditsUiEvent.OnOriginStoryChanged -> _uiState.update { it.copy(originStory = event.value) }
            is SuggestEditsUiEvent.OnGrowthPlaybookChanged -> _uiState.update { it.copy(growthPlaybook = event.value) }
            is SuggestEditsUiEvent.OnCategoryChanged -> _uiState.update { it.copy(category = event.value) }
            is SuggestEditsUiEvent.OnPublisherChanged -> _uiState.update { it.copy(publisher = event.value) }
            is SuggestEditsUiEvent.OnRevenueChanged -> _uiState.update { it.copy(revenue = event.value) }
            is SuggestEditsUiEvent.OnDownloadsChanged -> _uiState.update { it.copy(downloads = event.value) }
            is SuggestEditsUiEvent.OnNoteChanged -> _uiState.update { it.copy(note = event.value) }
            SuggestEditsUiEvent.OnSubmitClicked -> submitSuggestion()
        }
    }

    private fun submitSuggestion() {
        viewModelScope.launch {
            val currentState = _uiState.value

            if (currentState.name.isBlank()) {
                _uiState.update { it.copy(errorMessage = "Please enter an App Name.") }
                return@launch
            }

            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }

            val userResult = userRepository.currentUser.firstOrNull()
            val user = userResult?.getOrNull()

            val userId = user?.id ?: "guest_${Clock.System.now().toEpochMilliseconds()}"
            val userEmail = user?.email
            val userDisplayName = user?.displayName ?: if (user?.isAnonymous != false) "Anonymous User" else null

            val original = currentState.originalStory
            val isNewStory = storyId == "new" || storyId.isBlank() || original == null

            val suggestion = AppStorySuggestion(
                storyId = if (isNewStory) "new_${Clock.System.now().toEpochMilliseconds()}" else storyId,
                storyName = if (isNewStory) currentState.name else original.name,
                userId = userId,
                userEmail = userEmail,
                userDisplayName = userDisplayName,
                timestamp = Clock.System.now().toEpochMilliseconds(),
                status = "pending",
                suggestedName = currentState.name.takeIf { isNewStory || it != original.name },
                suggestedOneLiner = currentState.oneLiner.takeIf { isNewStory || it != original.oneLiner },
                suggestedTechStack = currentState.techStack.takeIf { isNewStory || it != original.techStack },
                suggestedOriginStory = currentState.originStory.takeIf { isNewStory || it != original.originStory },
                suggestedGrowthPlaybook = currentState.growthPlaybook.takeIf { isNewStory || it != original.growthPlaybook },
                suggestedCategory = currentState.category.takeIf { isNewStory || it != original.category },
                suggestedPublisher = currentState.publisher.takeIf { isNewStory || it != original.publisher },
                suggestedRevenue = currentState.revenue.takeIf { isNewStory || it != original.revenue },
                suggestedDownloads = currentState.downloads.takeIf { isNewStory || it != original.downloads },
                note = currentState.note.takeIf { it.isNotBlank() },
            )

            val result = appStoryRepository.submitSuggestion(suggestion)
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        isSubmittedSuccessfully = true,
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = "Failed to submit suggestion: ${result.exceptionOrNull()?.message ?: "Please try again."}",
                    )
                }
            }
        }
    }
}
