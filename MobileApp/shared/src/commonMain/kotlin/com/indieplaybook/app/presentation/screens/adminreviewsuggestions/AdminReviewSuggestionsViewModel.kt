package com.indieplaybook.app.presentation.screens.adminreviewsuggestions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indieplaybook.app.data.repository.AppStoryRepository
import com.indieplaybook.app.domain.model.AppStory
import com.indieplaybook.app.domain.model.AppStorySuggestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminReviewSuggestionsViewModel(
    private val appStoryRepository: AppStoryRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminReviewSuggestionsUiState())
    val uiState: StateFlow<AdminReviewSuggestionsUiState> = _uiState.asStateFlow()

    init {
        loadPendingSuggestions()
    }

    private fun loadPendingSuggestions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = appStoryRepository.getPendingSuggestions()
            if (result.isSuccess) {
                val suggestions = result.getOrDefault(emptyList())
                val items = suggestions.map { suggestion ->
                    val story = appStoryRepository.getStoryById(suggestion.storyId).first()
                    SuggestionItemUiState(suggestion = suggestion, story = story)
                }
                _uiState.update {
                    it.copy(
                        suggestions = items,
                        isLoading = false,
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to load suggestions.",
                    )
                }
            }
        }
    }

    fun onUiEvent(event: AdminReviewSuggestionsUiEvent) {
        when (event) {
            is AdminReviewSuggestionsUiEvent.OnApproveClicked -> approveSuggestion(event.suggestion, event.story)
            is AdminReviewSuggestionsUiEvent.OnRejectClicked -> rejectSuggestion(event.suggestionId)
            AdminReviewSuggestionsUiEvent.OnRefresh -> loadPendingSuggestions()
        }
    }

    private fun approveSuggestion(suggestion: AppStorySuggestion, story: AppStory) {
        viewModelScope.launch {
            _uiState.update { it.copy(isActionInProgress = true, errorMessage = null, successMessage = null) }
            val result = appStoryRepository.approveSuggestion(suggestion, story)
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        isActionInProgress = false,
                        successMessage = "Suggestion approved successfully!",
                    )
                }
                loadPendingSuggestions()
            } else {
                _uiState.update {
                    it.copy(
                        isActionInProgress = false,
                        errorMessage = "Failed to approve suggestion.",
                    )
                }
            }
        }
    }

    private fun rejectSuggestion(suggestionId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isActionInProgress = true, errorMessage = null, successMessage = null) }
            val result = appStoryRepository.rejectSuggestion(suggestionId)
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        isActionInProgress = false,
                        successMessage = "Suggestion rejected.",
                    )
                }
                loadPendingSuggestions()
            } else {
                _uiState.update {
                    it.copy(
                        isActionInProgress = false,
                        errorMessage = "Failed to reject suggestion.",
                    )
                }
            }
        }
    }
}
